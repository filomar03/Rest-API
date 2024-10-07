package it.unimib.sd2024;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import it.unimib.sd2024.models.Domain;
import it.unimib.sd2024.models.Order;
import it.unimib.sd2024.models.RegistrationStatus;
import it.unimib.sd2024.models.User;
import it.unimib.sd2024.requests.DomainConfirmRequest;
import it.unimib.sd2024.requests.DomainRenewRequest;
import it.unimib.sd2024.responses.OrdersResponse;

public class LogicHandler {
    private static volatile LogicHandler instance;
    private final Map<String, Domain> domainLocks;
    private final Lock mapLock;

    private LogicHandler() {
        domainLocks = new ConcurrentHashMap<>();
        mapLock = new ReentrantLock();
    }

    public static LogicHandler getInstance() {
        LogicHandler _instance = instance;
        if (_instance == null) {
            synchronized (LogicHandler.class) {
                _instance = LogicHandler.instance;
                if (_instance == null) {
                    instance = _instance = new LogicHandler();
                }
            }
        }

        return _instance;
    }

    public String getAllDomains() {
        try {
            String result = DbConnection.performQuery("get domini");
            if (result.startsWith("10")) {
                result = result.substring(2);
                System.out.println("getAllDomains" + result);
                return result;
            }
        } catch (IOException ex) {}

        return null;
    }

    private Domain getDomainFromDb(String domainName) {
        try {
            String domainStr = DbConnection.performQuery("get domini id " + domainName);
            if (domainStr.equals("11")) {
                return new Domain(domainName, "available", null, null, null);
            } else if (domainStr.startsWith("10")) {
                String domainJson = domainStr.substring(domainStr.indexOf(":") + 1, domainStr.length() - 1);
                System.out.println("getDomainFromDb: " + domainStr);
                System.out.println("getDomainFromDb: " + domainJson);
                System.out.println(Domain.fromJson(domainJson).getName());
                return Domain.fromJson(domainJson);
            }
        } catch (IOException e) { 
    }
        
        return null;
    }

    public Domain fetchDomainInfos(String domainName) {
        mapLock.lock();
        try {
            Domain lockedDomain = domainLocks.get(domainName);
            if (lockedDomain != null) {
                if (!lockedDomain.getEnd().isAfter(Instant.now())) {
                    domainLocks.remove(domainName);
                } else {
                    return lockedDomain;
                }
            }
        } finally {
            mapLock.unlock();
        }

        return getDomainFromDb(domainName);
    }

    public RegistrationStatus tlockDomain(String domainName, String userId) {
        if (verifyUser(userId) != 1) {
            return RegistrationStatus.BAD_REQUEST;
        }
        
        switch (getDomainFromDb(domainName).getState()) {
            case "pending" -> {
                return RegistrationStatus.ALREADY_LOCKED;
            }
            case "not available" -> {
                return RegistrationStatus.NOT_AVAILABLE;
            }
            case "available" -> {}
        }        

        //to make this operation "atomic" all operations on the map should acquire this lock
        //enforcing this via a wrapper function may be better, but since this is the only method 
        //that modifies the map, it's not really needed
        mapLock.lock(); 
        try {
            Instant lockStart = Instant.now();
            Instant lockEnd = LockUtils.computeLockEnd(lockStart);

            Domain lockedDomain = domainLocks.get(domainName);
            System.out.println(lockedDomain);
            if (lockedDomain == null || lockedDomain.getEnd().isBefore(lockStart)) {
                domainLocks.put(domainName, new Domain(domainName, "pending", userId, lockStart, lockEnd));
                System.out.println("locked: " + domainName);
            } else {
                return RegistrationStatus.ALREADY_LOCKED;
            }
        } finally {
            mapLock.unlock();
        }

        if (getDomainFromDb(domainName).getState().equals("not available")) {
            domainLocks.remove(domainName);
            return RegistrationStatus.NOT_AVAILABLE;
        } else {
            return RegistrationStatus.OK;
        }
    }

    public RegistrationStatus registerDomain(DomainConfirmRequest regReq, String userId) {
        if (verifyUser(userId) != 1) {
            return RegistrationStatus.BAD_REQUEST;
        }

        Domain lockedDomain = domainLocks.get(regReq.getDomain());
        System.out.println("locked?: " + lockedDomain);
        if (lockedDomain != null && lockedDomain.getOwner().equals(userId)) {
            if (lockedDomain.getEnd().isBefore(Instant.now())) {
                return RegistrationStatus.TIMED_OUT;
            } else {
                Domain dbDomain = new Domain(regReq.getDomain(), "not available", userId, Instant.now(), regReq.getEnd());
                Order dbOrder = new Order(Instant.now(), regReq.getDomain(), userId, Instant.now(), regReq.getEnd(), 
                    new Random().nextFloat(10)
                );
                System.out.println("confirmed: " + dbDomain.getName());
                try {
                    DbConnection.performQuery("add Document domini " + dbDomain.getName() + " " + dbDomain.toJson());
                } catch (IOException ex) {
                }
                domainLocks.remove(regReq.getDomain());
                return RegistrationStatus.OK;
            }
        } else {
            return RegistrationStatus.NOT_LOCKED;
        }
    }

    public RegistrationStatus renewDomain(DomainRenewRequest renReq, String userId) {
        if (verifyUser(userId) != 1) {
            return RegistrationStatus.BAD_REQUEST;
        }

        Domain domain = fetchDomainInfos(renReq.getDomain());
        if (domain != null && domain.getOwner().equals(renReq.getUserId())) {
            domain.setEnd(renReq.getEnd());
            try {
                DbConnection.performQuery("update domini " + domain.getName() + " " + domain.toJson());
            } catch (IOException ex) {
            }
            Order dbOrder = new Order(domain.getStart(), renReq.getDomain(), userId, domain.getStart(), renReq.getEnd(), 
                new Random().nextFloat(10)
            );
            System.out.println("confirmed: " + domain.getName());
            return RegistrationStatus.OK;
        } else {
            return RegistrationStatus.NOT_OWNER;
        }
    }

    public OrdersResponse fetchUserOrders(String userId) {
        if (verifyUser(userId) != 1) {
            return new OrdersResponse("user not found", new ArrayList<>());
        }
        
        try {
            String result = DbConnection.performQuery("get ordini userId " + userId);
            return new OrdersResponse(userId, new ArrayList<>());
        } catch (IOException e) {
            return new OrdersResponse(userId, new ArrayList<>());
        }
    }

    public int verifyUser(String userId) {
        try {
            String result = DbConnection.performQuery("get utenti id " + userId);
            System.out.println("verifyUser: " + result);
            if (result.equals("11")) {
                return 0;
            } else {
                return 1;
            }
        } catch (IOException ex) {
            return -1;
        }
    }

    public boolean registerUser(User user) {
        if (verifyUser(user.getMail()) == 0) {
            try {
                String query = "add Document utenti " + user.getMail() + " name " + user.getName() + " surname " + user.getSurname();
                String result = DbConnection.performQuery(query);
                if (result.equals("10")) {
                    return true;
                }
            } catch (IOException ex) {

            }
        }

        return false;
    }
}
