package com.example.GymApp.Model;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.GymApp.Adapter.ExerciseAdapter;
import com.example.GymApp.Data.ExerciseManager;
import com.example.GymApp.Exercise;
import com.example.GymApp.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SelectedMuscleGroupActivity extends AppCompatActivity {
    private static final String TAG = "SelectedMuscleGroup";

    private String userId;
    private List<Exercise> exercisesByMuscleGroup = new ArrayList<>();
    private ExerciseAdapter exerciseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercises_main_page);

        // Retrieve userID from intent
        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getStringExtra("userId");
            // Get the list of exercises from the intent
            exercisesByMuscleGroup = (List<Exercise>) getIntent().getSerializableExtra("exercisesByMuscleGroup");
        }

        // Find RecyclerView
        RecyclerView recyclerView = findViewById(R.id.exercises_by_muscle_group);

        // Set layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create adapter
        exerciseAdapter = new ExerciseAdapter(this, exercisesByMuscleGroup);

        // Set adapter
        recyclerView.setAdapter(exerciseAdapter);

        // Load user data
       // loadUserData(userId);
    }

    private void loadUserData(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(userId);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    // Clear the existing list of exercises
                    exercisesByMuscleGroup.clear();

                    // Update UI with user information
                    List<String> userEquipment = (List<String>) document.get("equipment");
                    if (userEquipment != null) {
                        // Process the list here
                        ExerciseManager exerciseManager = new ExerciseManager(); // Create an instance of ExerciseManager
                        for (String equipment : userEquipment) {
                            Log.d(TAG, "Equipment: " + equipment);
                            // Get exercises by equipment
                            List<Exercise> exercisesByEquipment = exerciseManager.getExercises(); // Pass the equipment to getExercises method
                            exercisesByMuscleGroup.addAll(exercisesByEquipment);
                        }
                        // Notify adapter that data set has changed
                        exerciseAdapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "No equipment data found");
                    }
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    public void selectedExercise(View view) {
        TextView exerciseNameTextView = view.findViewById(R.id.ExerciseName); // Assuming ExerciseName is inside the clicked view
        ImageView exercisePhotoImageView = view.findViewById(R.id.exercisePhoto);

            String exerciseName = exerciseNameTextView.getText().toString();
            Object tag = exercisePhotoImageView.getTag();
            int exercisePhoto = tag instanceof Integer ? (int) tag : 3;

            // Start ExercisePageActivity and pass the exercise name and photo
            Intent selectedExerciseIntent = new Intent(SelectedMuscleGroupActivity.this, ExercisePageActivity.class);
            selectedExerciseIntent.putExtra("exerciseName", exerciseName);
            selectedExerciseIntent.putExtra("exercisePhoto", exercisePhoto);
            startActivity(selectedExerciseIntent);

    }

}
