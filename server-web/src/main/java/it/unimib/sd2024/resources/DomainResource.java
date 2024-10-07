package it.unimib.sd2024.resources;

import it.unimib.sd2024.LogicHandler;
import it.unimib.sd2024.models.Domain;
import it.unimib.sd2024.requests.DomainConfirmRequest;
import it.unimib.sd2024.requests.DomainLockingRequest;
import it.unimib.sd2024.requests.DomainRenewRequest;
import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("domains")
public class DomainResource {
    static LogicHandler logicHandler = LogicHandler.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDomains() {
        String result = logicHandler.getAllDomains();
        if (result == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } else {
            return Response.ok(result).build();
        }
    }

    @GET
    @Path("/{domain}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkDomainAvailability(@PathParam("domain") String name) {
        Domain domain = logicHandler.fetchDomainInfos(name);

        if (domain == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } else {
            return Response.ok(domain).build();
        }
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerDomain(@HeaderParam("userId") String userId, DomainLockingRequest request) {
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();

        switch (logicHandler.tlockDomain(request.getDomain(), userId)) {
            case OK -> {
                jsonBuilder.add("message", "domain '" + request.getDomain() + "' locked succesfully");    
                return Response.ok(jsonBuilder.build()).build();
            }
            case ALREADY_LOCKED -> {
                jsonBuilder.add("message", "domain '" + request.getDomain() + "' is already locked");
                return Response.status(Response.Status.CONFLICT).entity(jsonBuilder.build()).build();
            }
            case NOT_AVAILABLE -> {
                jsonBuilder.add("message", "domain '" + request.getDomain() + "' is already owned");
                return Response.status(Response.Status.CONFLICT).entity(jsonBuilder.build()).build();
            } 
            case BAD_REQUEST -> {
                jsonBuilder.add("message", "user needs to be registered");
                return Response.status(Response.Status.UNAUTHORIZED).entity(jsonBuilder.build()).build();
            } 
            default -> {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    @POST
    @Path("/confirm")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response confirmDomain(@HeaderParam("userId") String userId, DomainConfirmRequest request) {
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();

        switch (logicHandler.registerDomain(request, userId)) {
            case OK -> {
                jsonBuilder.add("message", "domain '" + request.getDomain() + "' registered succesfully");    
                return Response.ok(jsonBuilder.build()).build();
            }
            case TIMED_OUT -> {
                jsonBuilder.add("message", "the lock on the domain '" + request.getDomain() + "' is expired");
                return Response.status(Response.Status.CONFLICT).entity(jsonBuilder.build()).build();
            }
            case NOT_LOCKED -> {
                jsonBuilder.add("message", "cannot register domain '" + request.getDomain() + "' since it's not locked");
                return Response.status(Response.Status.CONFLICT).entity(jsonBuilder.build()).build();
            } 
            case BAD_REQUEST -> {
                jsonBuilder.add("message", "user needs to be registered");
                return Response.status(Response.Status.UNAUTHORIZED).entity(jsonBuilder.build()).build();
            } 
            default -> {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    @POST
    @Path("/renew")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response renewDomain(@HeaderParam("userId") String userId, DomainRenewRequest request) {
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();

        switch (logicHandler.renewDomain(request, userId)) {
            case OK -> {
                jsonBuilder.add("message", "domain '" + request.getDomain() + "' renewed succesfully");    
                return Response.ok().build();
            }
            case NOT_OWNER -> {
                jsonBuilder.add("message", "domain '" + request.getDomain() + "' can only be renewed by its owner");
                return Response.status(Response.Status.FORBIDDEN).entity(jsonBuilder.build()).build();
            }
            case BAD_REQUEST -> {
                jsonBuilder.add("message", "user needs to be registered");
                return Response.status(Response.Status.UNAUTHORIZED).entity(jsonBuilder.build()).build();
            } 
            default -> {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        }
    }
}