package ru.android.messenger.model;

public class User {

    private String login;
    private String password;
    private String email;
    private String firstName;
    private String surname;

    public User(String login, String password, String email, String firstName, String surname) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.surname = surname;
    }
}
