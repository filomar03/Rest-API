package it.unimib.sd2024.models;

public enum DomainState {
    AVAILABLE("available"),
    TMP_LOCKED("pending"),
    NOT_AVAILABLE("not available");

    private final String state;

    private DomainState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return state;
    }
}
