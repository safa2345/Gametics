package com.mir.gametics;

public class LogInStatus {

    private boolean isLoggedIn = false;

    private final static LogInStatus INSTANCE = new LogInStatus();

    private LogInStatus() {}

    public static LogInStatus getInstance() {
        return INSTANCE;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }
}
