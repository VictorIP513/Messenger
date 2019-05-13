package ru.android.messenger.model;

import android.graphics.Bitmap;

public class UserFromView {

    private Bitmap userPhoto;
    private String login;
    private String firstName;
    private String surname;

    public UserFromView(Bitmap userPhoto, String login, String firstName, String surname) {
        this.userPhoto = userPhoto;
        this.login = login;
        this.firstName = firstName;
        this.surname = surname;
    }

    public Bitmap getUserPhoto() {
        return userPhoto;
    }

    public String getLogin() {
        return login;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }
}
