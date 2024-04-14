package com.example.GymApp.Model;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.GymApp.Adapter.Adapter;
import com.example.GymApp.Data.ExerciseManager;
import com.example.GymApp.Data.MuscleGroupData;
import com.example.GymApp.Exercise;
import com.example.GymApp.MuscleGroup;
import com.example.GymApp.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ManageExercisesActivity extends AppCompatActivity {

    private String userId; // Declare userId as a class member
    public List<String> equipment;

    private ExerciseManager exerciseManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muscle_group_main_page);
        List<String> equipmentList = getIntent().getStringArrayListExtra("equipments");
        if (equipmentList != null) {
            // Use 'equipmentList' as needed
            exerciseManager = new ExerciseManager(equipmentList);
        }
        else {
            exerciseManager = new ExerciseManager();
        }
        // Ensure the key here matches what was used in SignInActivity
        userId = getIntent().getStringExtra("userId"); // Assign userId here

        if (userId == null) {
            userId = "User"; // Default value if userId is null
        }

        //load the user data

        TextView title = findViewById(R.id.TextViewUsername);
        title.setText("Hello! We have some exercises for you.");

        RecyclerView recyclerView = findViewById(R.id.res);

        // Assuming you have a method to get a list of exercises
        List<MuscleGroup> exerciseList = MuscleGroupData.getMuscleGroups(); // Get your exercise data
        Adapter adapter = new Adapter(exerciseList, this); // Pass the data to the adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Set layout manager
        recyclerView.setAdapter(adapter); // Set adapter to the RecyclerView
    }

    public void SelectedMuscleGroup(View view) {
        TextView cardTitle = view.findViewById(R.id.muscleGroupTitle);
        String muscleGroupTitle = cardTitle.getText().toString();

        List<Exercise> exercisesByMuscleGroup = exerciseManager.GetExercise(muscleGroupTitle);

        Intent exerciseIntent = new Intent(ManageExercisesActivity.this, SelectedMuscleGroupActivity.class);
        exerciseIntent.putExtra("userId", userId);
        exerciseIntent.putParcelableArrayListExtra("exercisesByMuscleGroup", new ArrayList<>(exercisesByMuscleGroup));
        startActivity(exerciseIntent);
    }


    // need to pass userID and move to the next activity
}
