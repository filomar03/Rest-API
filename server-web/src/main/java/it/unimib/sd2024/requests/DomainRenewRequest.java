package it.unimib.sd2024.requests;

import java.time.Instant;

public class DomainRenewRequest {
    private String domain;
    private String userId;
    private Instant end;

    public DomainRenewRequest(String domainName, String userId, Instant end) {
        this.domain = domainName;
        this.userId = userId;
        this.end = end;
    }

    public DomainRenewRequest() {}

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domainName) {
        this.domain = domainName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Instant getEnd() {
        return end;
    }

    public void setEnd(Instant start) {
        this.end = start;
    }
}
