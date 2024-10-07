package it.unimib.sd2024.requests;

import java.time.Instant;

public class DomainConfirmRequest {
    private String domain;
    private Instant end;

    public DomainConfirmRequest(String domainName, Instant end) {
        this.domain = domainName;
        this.end = end;
    }

    public DomainConfirmRequest() {}

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domainName) {
        this.domain = domainName;
    }

    public Instant getEnd() {
        return end;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }
}
