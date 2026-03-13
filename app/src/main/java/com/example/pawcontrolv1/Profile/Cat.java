package com.example.pawcontrolv1.Profile;

public class Cat {
    private String name;
    private String breed;
    private String age;
    private int photoResId;     // for local drawables
    private String photoUrl;    // for remote images (optional)
    private boolean isFavourite;

    // Constructor for local drawable
    public Cat(String name, String breed, String age, int photoResId) {
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.photoResId = photoResId;
        this.isFavourite = false;
    }

    // Constructor for remote URL
    public Cat(String name, String breed, String age, String photoUrl) {
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.photoUrl = photoUrl;
        this.isFavourite = false;
    }

    public String getName()            { return name; }
    public String getBreed()           { return breed; }
    public String getAge()             { return age; }
    public int getPhotoResId()         { return photoResId; }
    public String getPhotoUrl()        { return photoUrl; }
    public boolean isFavourite()       { return isFavourite; }
    public void setFavourite(boolean f){ this.isFavourite = f; }
}