package com.example.GymApp.Model;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.GymApp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class SignUpActivity extends AppCompatActivity {
    AtomicReference<List<String>> selectedEquipmentDb = new AtomicReference<>(new ArrayList<>());

    public interface GymEquipmentSelectionListener {
        void onEquipmentSelected(List<String> selectedEquipment);
    }
    private void updateEquipmentList(List<String> newEquipment) {
        selectedEquipmentDb.updateAndGet(currentEquipment -> {
            // Check if "Regular" is already included, if not, add it
            if (!currentEquipment.contains("Regular")) {
                currentEquipment.add("Regular");
            }

            // Now handle new equipment; adding only those not already present
            for (String equipment : newEquipment) {
                if (!currentEquipment.contains(equipment)) {
                    currentEquipment.add(equipment);
                }
            }
            return currentEquipment;
        });

        Log.d("UpdatedEquipment", "Equipment list updated: " + selectedEquipmentDb.get());
    }


    private void storeUserInformationWithEquipment(String userId, String email, String name, String age, String weight, String height, AtomicReference<List<String>> selectedEquipmentDb) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("name", name);
        user.put("age", age);
        user.put("weight", weight);
        user.put("height", height);
        user.put("equipment", selectedEquipmentDb.get()); // Use .get() to access the actual List<String>

        db.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot added with ID: " + userId))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

    private void registerUser(String email, String password, AtomicReference<List<String>> selectedEquipmentDb, String name, String age, String weight, String height) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (weight.isEmpty() )
            weight = "0";
        if (height.isEmpty())
            height = "0";
        String finalWeight = weight;
        String finalHeight = height;
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        Toast.makeText(SignUpActivity.this, "Successfully Registered  .",
                                Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        storeUserInformationWithEquipment(user.getUid(), email, name, age, finalWeight, finalHeight, selectedEquipmentDb);
                        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                        startActivity(intent);
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(SignUpActivity.this, "Registered Failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    protected void showGymEquipmentDialog(final GymEquipmentSelectionListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Gym Equipment");

        final String[] gymEquipment = getResources().getStringArray(R.array.gym_equipment_options);
        final boolean[] checkedItems = new boolean[gymEquipment.length]; // false default
        final ArrayList<String> userSelections = new ArrayList<>();

        builder.setMultiChoiceItems(gymEquipment, checkedItems, (dialog, which, isChecked) -> {
            // Add or remove the item from the user's selections based on isChecked
            if (isChecked) {
                userSelections.add(gymEquipment[which]);
            } else {
                userSelections.remove(gymEquipment[which]);
            }
        });

        builder.setPositiveButton("OK", (dialog, which) -> {
            if (listener != null) {
                listener.onEquipmentSelected(userSelections);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        List<String> initialEquipment = Arrays.asList();
        updateEquipmentList(initialEquipment);
        Button selectEquipmentButton = findViewById(R.id.equipment_list);
        selectEquipmentButton.setOnClickListener(v -> showGymEquipmentDialog(selectedEquipment -> {
            updateEquipmentList(selectedEquipment);
            for (String equipment : selectedEquipment) {
                Log.d("SelectedEquipment", equipment);
            }
        }));
        EditText editTextRegisterUsername = findViewById(R.id.editTextRegisterName);
        EditText editTextRegisterPassword = findViewById(R.id.editTextRegisterPassword);
        EditText editTextRegisterName = findViewById(R.id.editTextUsername);
        EditText editTextRegisterAge = findViewById(R.id.editTextUpdateAge);
        EditText editTextRegisterHeight = findViewById(R.id.editTextUpdateHeight);
        EditText editTextRegisterWeight = findViewById(R.id.editTextUpdateWeight);
        Button buttonSubmitRegister = findViewById(R.id.buttonSubmitUpdateInfo);
        // Inside SignUpFragment's buttonSubmitRegister.setOnClickListener
        buttonSubmitRegister.setOnClickListener(v -> {
            // Extract user input
            String username = editTextRegisterUsername.getText().toString().trim();
            String password = editTextRegisterPassword.getText().toString().trim();
            String name = editTextRegisterName.getText().toString().trim();
            String Age = editTextRegisterAge.getText().toString().trim();
            String height = editTextRegisterHeight.getText().toString().trim();
            String Weight = editTextRegisterWeight.getText().toString().trim();
            //need to enter the data we collected to Firebase
            registerUser(username, password, selectedEquipmentDb, name, Age, Weight, height);
        });

    }
}
