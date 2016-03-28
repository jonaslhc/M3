package com.interaxon.test.libmuse.Data;

/* this data structure contains the profile data of users
 * username: user's name, must be unique
 * name: first and last name as user wants
 * ...
 * password: for privacy */

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

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

    // constructor for adding new meditation session
    public ProfileData(String username, String password, String name, int age, String email,
                       String date, int first, int stroop_index, int stroop_count,
                       int meditation_session_num, int meditation_index, int meditation_count,
                       double accuracy, double reaction_time, String meditation) {

        this.username = username;
        this.name = name;
        this.password = password;
        this.age = age;
        this.email = email;
        this.date = date;

        this.stroop_count = stroop_count;
        this.stroop_index = stroop_index;
        this.accuracy = accuracy;
        this.reaction_time = reaction_time;

        this.meditation_count = meditation_count;
        this.meditation_index = meditation_index;
        this.meditation_session_num = meditation_session_num;
        this.meditation = meditation;

    }

    // constructor for adding new meditation session
    public ProfileData(String username,
                       int meditation_session_num, int meditation_index, int meditation_count,
                       String meditation) {

        this.username = username;

        this.stroop_index = -1;

        this.meditation_count = meditation_count;
        this.meditation_index = meditation_index;
        this.meditation_session_num = meditation_session_num;
        this.meditation = meditation;

    }

    // constructor for adding new stroop session
    public ProfileData(String username,
                       int stroop_index, int stroop_count,
                       double accuracy, double reaction_time) {

        this.username = username;

        this.stroop_count = stroop_count;
        this.stroop_index = stroop_index;
        this.accuracy = accuracy;
        this.reaction_time = reaction_time;

        this.meditation_index = -1;
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
    public void setMeditation(String meditation) {
        this.meditation = meditation;
    }
    public void setStroop(double time, double accuracy) {
        this.reaction_time = time;
        this.accuracy = accuracy;
    }
    public void updateFirst() {
        this.first = false;
    }

    // get functions for data
    public String getUsername() {return username;}
    public String getName() {return name;}
    public String getPassword() {return password;}
    public String getEmail() {return email;}
    public int getAge () {return age;}
    public String getDate() {return date;}
    public boolean getFirst() {return first;}

    public int getStroopIndex() {return stroop_index;}
    public int getStroopCount () {return stroop_count;}
    public double getAccuracy() {return accuracy;}
    public double getReactionTime() {return reaction_time;}

    public int getMeditationIndex() {return meditation_index;}
    public int getMeditationCount() {return meditation_count;}
    public int getMeditationSessionNum() {return meditation_session_num;}
    public String getMeditation() {return meditation;}
    public ArrayList<Double> getMeditationDouble() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Double>>() {}.getType();

        ArrayList<Double> outputMeditation = gson.fromJson(this.meditation, type);

        return outputMeditation;
    }
}