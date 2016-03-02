package com.interaxon.test.libmuse;

/* this data structure contains the profile data of users
 * username: user's name, must be unique
 * name: first and last name as user wants
 * ...
 * password: for privacy */

public class ProfileData {
    private String username;
    private String name;
    private String email;
    private String password;
    private String age;

    // empty constructor
    public ProfileData() {

    }

    // add a new data
    public ProfileData(String username, String password, String name) {
        this.username = username;
        this.name = name;
        this.password = password;
    }

    // get functions for data
    public String getUsername() {
        return username;
    }
    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }

}