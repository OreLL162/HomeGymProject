package com.example.GymApp.Data;

import com.example.GymApp.Exercise;
import com.example.GymApp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ExerciseManager {
    private Map<String, Integer> categories;
    private List<Exercise> exercises;

    //ExerciseManager ExerciseList = new ExerciseManager();

    private List<String> equipment;

    public ExerciseManager() {
        // Initialize categories
        categories = new HashMap<>();
        categories.put("Chest Exercises", 1);
        categories.put("Back Exercises", 2);
        categories.put("Shoulder Exercises", 3);
        categories.put("Biceps Exercises", 4);
        categories.put("Triceps Exercises", 5);
        categories.put("ABS Exercises", 6);
        categories.put("Legs Exercises", 7);

        // Initialize the exercise list
        exercises = new ArrayList<>();

        // Call initExerciseList to populate the exercise list
        initExerciseList();
    }
    public ExerciseManager(List<String> availableEquipment) {
        this();
        this.equipment = availableEquipment;
        filterExercisesByEquipment();
    }
    private void filterExercisesByEquipment() {
        List<Exercise> filteredList = new ArrayList<>();
        for (Exercise exercise : this.exercises) {
            if (equipment.contains(exercise.getEquipment())) {
                filteredList.add(exercise);
            }
        }
        this.exercises = filteredList;  // Replace all exercises with only those that can be performed
    }

    public void addExercise(String name, String category, String Equipment,int photo) {
        if (categories.containsKey(category)) {
            exercises.add(new Exercise(name, category, Equipment,photo));
        } else {
            System.out.println("Unknown category: " + category);
        }
    }

    public Map<String, Integer> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, Integer> categories) {
        this.categories = categories;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public List<String> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<String> equipment) {
        this.equipment = equipment;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    // Example method to print all exercises
    public void printExercises() {
        for (Exercise exercise : exercises) {
            System.out.println("Exercise: " + exercise.getName() + ", Category: " + exercise.getCategory());
        }
    }

    public List<Exercise> GetExercise (String muscleCategory){
        List<Exercise> exerciseList = new ArrayList();
        for (Exercise exercise : exercises) {
            if (Objects.equals(exercise.getCategory(), muscleCategory)) {
                exerciseList.add(exercise);
            }
        }
        return exerciseList;
    }

    void initExerciseList() {

        // Initialize the exercise list

        // Legs Exercises

        this.addExercise("BARBELL SQUAT", "Legs Exercises", "Bar", R.drawable.barbell_squat);

        this.addExercise("Dumbbell Deadlifts", "Legs Exercises", "Dumbbell", R.drawable.dumbbell_deadlifts);

        this.addExercise("Dumbbell Goblet Squat", "Legs Exercises", "Dumbbell", R.drawable.dumbbell_goblet_squat);

        this.addExercise("Dumbbell Lunges", "Legs Exercises", "Dumbbell", R.drawable.dumbbell_lunges);

        this.addExercise("Dumbbell Split Jump", "Legs Exercises", "Dumbbell", R.drawable.dumbbell_split_jump);

        this.addExercise("Dumbbell Squat", "Legs Exercises", "Dumbbell", R.drawable.dumbbell_squat);

        this.addExercise("Curtsey Squat", "Legs Exercises", "Regular", R.drawable.curtsey_squat);

        this.addExercise("Kettlebell Swings", "Legs Exercises", "Kettlebell", R.drawable.kettlebell_swings);

        this.addExercise("Kettlebell Deadlift", "Legs Exercises", "Kettlebell", R.drawable.kettlebell_deadlift);

        // ABS Exercises

        this.addExercise("Bicycle Crunch", "ABS Exercises", "Regular", R.drawable.bicycle_crunch);

        this.addExercise("Cross Crunch", "ABS Exercises", "Regular", R.drawable.cross_crunch);

        this.addExercise("Crunch", "ABS Exercises", "Regular", R.drawable.crunch);


    // Triceps Exercises

        this.addExercise("Band Pushdown", "Triceps Exercises", "Strip", R.drawable.band_pushdown);

        this.addExercise("Band Skull Crusher", "Triceps Exercises", "Strip", R.drawable.band_skull_crusher);

        this.addExercise("Body Ups", "Triceps Exercises", "Regular", R.drawable.body_ups);

        this.addExercise("CHAIR DIPS", "Triceps Exercises", "Regular", R.drawable.chair_dips);

        this.addExercise("Dumbbell Triceps Extension", "Triceps Exercises", "Dumbbells", R.drawable.dumbbell_triceps_extension);


    // Back Exercises

        this.addExercise("Barbell Bent Over Row", "Back Exercises", "Bar", R.drawable.barbell_bent_over_row);

        this.addExercise("Bent Over Dumbbell Row", "Back Exercises", "Dumbbells", R.drawable.bent_over_dumbbell_row);

        this.addExercise("Kettlebell Bent Over Row", "Back Exercises", "Kettlebell", R.drawable.kettlebell_bent_over_row);


    // Chest Exercises

        this.addExercise("Dumbbell Upward Fly", "Chest Exercises", "Dumbbells", R.drawable.dumbbell_upward_fly);

        this.addExercise("Close Grip Dumbbell Push Up", "Chest Exercises", "Dumbbells", R.drawable.close_grip_dumbbell_push_up);

        this.addExercise("Incline Push Up", "Chest Exercises", "Regular", R.drawable.incline_push_up);

        this.addExercise("Push Up", "Chest Exercises", "Regular", R.drawable.push_up);

        this.addExercise("Reverse Push Up", "Chest Exercises", "Regular", R.drawable.reverse_push_up);

        this.addExercise("Kettlebell Chest Press on the Floor", "Chest Exercises", "Kettlebell", R.drawable.kettlebell_chest_press_on_the_floor);


    // Biceps Exercises

        this.addExercise("Hammer Curl", "Biceps Exercises", "Dumbbells", R.drawable.hammer_curl);

        this.addExercise("Waiter Curl", "Biceps Exercises", "Dumbbells", R.drawable.waiter_curl);

        this.addExercise("Zottman Curl", "Biceps Exercises", "Dumbbells", R.drawable.zottman_curl);

        this.addExercise("Barbell Curl", "Biceps Exercises", "Bar", R.drawable.barbell_curl);



        // Shoulder Exercises

        this.addExercise("Bent Over Lateral Raise", "Shoulder Exercises", "Dumbbells", R.drawable.bent_over_lateral_raise);

        this.addExercise("Dumbbell Lateral Raise", "Shoulder Exercises", "Dumbbells", R.drawable.dumbbell_lateral_raise);

        this.addExercise("Dumbbell Front Raise", "Shoulder Exercises", "Dumbbells", R.drawable.dumbbell_front_raise);

        this.addExercise("Arm Circles Shoulders", "Shoulder Exercises", "Regular", R.drawable.arm_circles_shoulders);

        this.addExercise("Bodyweight Military Press", "Shoulder Exercises", "Regular", R.drawable.bodyweight_military_press);

        this.addExercise("Band Pull Apart", "Shoulder Exercises", "Strip", R.drawable.band_pull_apart);

        this.addExercise("Resistance Band Bent Over Rear Delt Fly", "Shoulder Exercises", "Strip", R.drawable.resistance_band_bent_over_rear_delt_fly);


    }
}