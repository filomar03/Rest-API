package it.unimib.sd2024.requests;

public class DomainLockingRequest {
    private String domain;

    public DomainLockingRequest(String domainName) {
        this.domain = domainName;
    }

    public DomainLockingRequest() {}

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domainName) {
        this.domain = domainName;
    }
}
