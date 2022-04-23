package com.example.pisocharge;

public class User {
    private int id;
    private String username, fullname, time;

    public User(int id, String username, String fullname, String time) {
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public String getTime() {
        return time;
    }

}
