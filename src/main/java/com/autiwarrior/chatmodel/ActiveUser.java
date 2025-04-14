package com.autiwarrior.chatmodel;

public class ActiveUser {

    private String username;
    private String session;

    public ActiveUser() {
    }

    public ActiveUser(String username, String session) {
        this.username = username;
        this.session = session;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
}
