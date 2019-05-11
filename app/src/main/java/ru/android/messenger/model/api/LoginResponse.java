package ru.android.messenger.model.api;

public class LoginResponse {

    private Status status;
    private String authenticationToken;

    public LoginResponse(Status status, String authenticationToken) {
        this.status = status;
        this.authenticationToken = authenticationToken;
    }

    public Status getStatus() {
        return status;
    }

    public String getAuthenticationToken() {
        return authenticationToken;
    }

    public enum Status {
        LOGIN_SUCCESSFUL,
        ACCOUNT_NOT_CONFIRMED,
        INVALID_LOGIN_OR_PASSWORD
    }
}
