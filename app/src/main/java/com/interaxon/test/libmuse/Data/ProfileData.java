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
    private int age;

    // determines whether the very first cognitive game was completed
    private boolean first;
    private String date;

    // stores accuracy data from cognitive game
    private int stroop_index;
    private int stroop_count;
    private double accuracy;
    private double reaction_time;

    // stores meditation data
    private int meditation_index;
    private int meditation_count;
    private int meditation_session_num;
    private String meditation;

    // empty constructor
    public ProfileData() {

    }

    // temp - remove eventually
    public ProfileData(String username, String password, String name) {
        this.username = username;
        this.name = name;
        this.password = password;
    }
    // add a new data
    public ProfileData(String username, String password, String name,
                       double accuracy, double reaction_time, String meditation) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.accuracy = accuracy;
        this.reaction_time = reaction_time;
        this.meditation = meditation;
    }


    // add a new user
    // initialize to be "first time user" until 1st stroop completed
    // initilize the stroop and meditation count to 0
    public ProfileData(String username, String password, String name, int age, String email) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.first = true;
        this.stroop_count = 0;
        this.meditation_count = 0;
    }

    public void incrMeditation() {
        this.meditation_count++;
    }
    public void incrStroop() {
        this.stroop_count++;
    }


    public void AddStroop (int index, double accuracy, double reaction_time) {
        this.stroop_index = index;
        this.accuracy = accuracy;
        this.reaction_time = reaction_time;
    }

    // get functions for data
    public String getUsername() {return username;}
    public String getName() {return name;}
    public String getPassword() {return password;}
    public String getEmail() {return email;}
    public int getAge () {return age;}
    public String getDate() {return date;}

    public int getStroopIndex() {return stroop_index;}
    public int getStroopCount () {return stroop_count;}
    public double getAccuracy() {return accuracy;}
    public double getReaction_time() {return reaction_time;}

    public int getMeditationIndex() {return meditation_index;}
    public int getMeditationCount() {return meditation_count;}
    public int getMeditationSessionNum() {return meditation_session_num;}
    public String getMeditation() {return meditation;}
}