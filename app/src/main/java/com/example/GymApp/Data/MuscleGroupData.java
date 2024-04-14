package com.example.GymApp.Data;

import com.example.GymApp.MuscleGroup;
import com.example.GymApp.R;

import java.util.ArrayList;
import java.util.List;

public class MuscleGroupData {

    // Inner class to represent exercise data for a muscle group
    public static class MuscleGroupExercise {
        private String title;
        private String description;
        private int photoResourceId;
        private static int numberOfSets = 3;
        private static int numberOfReps = 10;

        // Constructor
        public MuscleGroupExercise(String title, String description, int photoResourceId) {
            this.title = title;
            this.description = description;
            this.photoResourceId = photoResourceId;
        }

        // Getters and setters
        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public int getPhotoResourceId() {
            return photoResourceId;
        }

        public static int getNumberOfSets() {
            return numberOfSets;
        }

        public static int getNumberOfReps() {
            return numberOfReps;
        }

        public static void setNumberOfSets(int numberOfSets) {
            MuscleGroupExercise.numberOfSets = numberOfSets;
        }

        public static void setNumberOfReps(int numberOfReps) {
            MuscleGroupExercise.numberOfReps = numberOfReps;
        }
    }

    // Static arrays for images, names, and descriptions
    static Integer[] images = {
            R.drawable.chest, R.drawable.back, R.drawable.shoulder, R.drawable.biceps, R.drawable.triceps,
            R.drawable.abs, R.drawable.legs
    };

    static String[] names = {
            "Chest Exercises", "Back Exercises", "Shoulder Exercises", "Biceps Exercises", "Triceps Exercises",
            "ABS Exercises", "Legs Exercises"
    };

    static String[] descriptions = {
            "Strengthen chest muscles with effective exercises for definition.",
            "Build a strong back with exercises targeting key muscles.",
            "Strengthen and sculpt shoulder muscles for stability and definition.",
            "Define and strengthen your arm muscles.",
            "Sculpt and tone the back of your arms.",
            "Sculpt a strong core with targeted exercises.",
            "Build lower body strength with focused exercises."
    };

    // Static method to get the images array
    public static Integer[] getImages() {
        return images;
    }

    // Static method to get the names array
    public static String[] getNames() {
        return names;
    }

    // Static method to get the descriptions array
    public static String[] getDescriptions() {
        return descriptions;
    }

    // Static method to get the list of exercises
    public static List<MuscleGroup> getMuscleGroups() {
        return muscleGroups;
    }

    // Static method to set the images array
    public static void setImages(Integer[] images) {
        MuscleGroupData.images = images;
    }

    // Static method to set the names array
    public static void setNames(String[] names) {
        MuscleGroupData.names = names;
    }

    // Static method to set the descriptions array
    public static void setDescriptions(String[] descriptions) {
        MuscleGroupData.descriptions = descriptions;
    }

    // Static method to set the list of exercises
    public static void setMuscleGroups(List<MuscleGroup> muscleGroups) {
        MuscleGroupData.muscleGroups = muscleGroups;
    }

    // Static list to hold Exercise objects
    private static List<MuscleGroup> muscleGroups = new ArrayList<>();

    // Static initializer block to populate the exercise list
    static {
        for (int i = 0; i < images.length; i++) {
            muscleGroups.add(new MuscleGroup(images[i], names[i], descriptions[i], 3, 10));
        }
    }
}
