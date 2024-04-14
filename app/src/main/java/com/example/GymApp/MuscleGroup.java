package com.example.GymApp;

public class MuscleGroup {
    private int imagePath;
    private String title;
    private String description;



    public MuscleGroup(int imagePath, String title, String description, int numnerOfSet, int numnerOfReps) {
        this.imagePath = imagePath;
        this.title = title;
        this.description = description;

    }

    public int getImagePath() {
        return imagePath;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setImagePath(int imagePath) {
        this.imagePath = imagePath;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
