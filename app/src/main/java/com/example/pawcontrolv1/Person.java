package com.example.pawcontrolv1;

public class Person {
    private final String name;
    private final int    avatarRes; // drawable resource for profile picture

    public Person(String name, int avatarRes) {
        this.name      = name;
        this.avatarRes = avatarRes;
    }

    public String getName()     { return name; }
    public int    getAvatarRes(){ return avatarRes; }
}