package com.uclk.shani.youseelk.objects;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String email;
    public String password;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
