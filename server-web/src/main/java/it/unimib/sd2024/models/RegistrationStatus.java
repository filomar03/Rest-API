package it.unimib.sd2024.models;

public enum RegistrationStatus {
    OK(0),
    ALREADY_LOCKED(1),
    NOT_AVAILABLE(2),
    NOT_LOCKED(3),
    TIMED_OUT(4),
    NOT_OWNER(5),
    BAD_REQUEST(6);

    private final int code;

    private RegistrationStatus(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return String.valueOf(code);
    }
}
