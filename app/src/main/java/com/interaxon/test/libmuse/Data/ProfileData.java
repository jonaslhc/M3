package com.interaxon.test.libmuse.Data;

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
    private double accuracy, reaction_time;
    private String meditation;

    // empty constructor
    public ProfileData() {

    }

    // add a new data
    public ProfileData(String username, String password, String name, double accuracy, double reaction_time, String meditation) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.accuracy = accuracy;
        this.reaction_time = reaction_time;
        this.meditation = meditation;
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

    public double getAccuracy() {
        return accuracy;
    }

    public double getReaction_time() {
        return reaction_time;
    }

    public String getMeditation() {
        return meditation;
    }

}