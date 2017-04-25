package com.normanhoeller.beachesarefun.login;

/**
 * Created by norman on 23/04/17.
 */

public class User {

    private String id;
    private String email;
    private String token;
    private String errorMessage;

    public User(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public User(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public User(String id, String email, String token) {
        this.id = id;
        this.email = email;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
