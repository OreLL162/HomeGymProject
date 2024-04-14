package com.example.GymApp.Model;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.GymApp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class MyInfoActivity extends AppCompatActivity {

    private Button buttonSave;
    private String currentUser;
    AtomicReference<List<String>> selectedEquipmentDb = new AtomicReference<>(new ArrayList<>());

    @SuppressLint("RestrictedApi")
    private void updateUserInformationWithEquipment(String userId, String age, String weight, String height, List<String> selectedEquipment) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> updates = new HashMap<>();
        if (age != null) updates.put("age", age);
        if (weight != null) updates.put("weight", weight);
        if (height != null) updates.put("height", height);
        if (selectedEquipment != null) updates.put("equipment", selectedEquipment);

        db.collection("users").document(userId)
                .update(updates)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "User information updated with ID: " + userId))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }
    protected void showGymEquipmentDialog(final SignUpActivity.GymEquipmentSelectionListener listener) {
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

    private void updateEquipmentList(List<String> newEquipment , List<String> equipmentList) {
        selectedEquipmentDb.updateAndGet(v -> {
            // Start with a clean list that only includes "Regular"
            List<String> updatedEquipment = new ArrayList<>();
            if (equipmentList.contains("Regular") || newEquipment.contains("Regular")) {
                updatedEquipment.add("Regular");
            }

            // Add new equipment from the input list, avoiding duplicates
            for (String equipment : newEquipment) {
                if (!updatedEquipment.contains(equipment)) {
                    updatedEquipment.add(equipment);
                }
            }

            // Replace the current equipment list with the updated list
            return updatedEquipment;
        });

        // Log the updated equipment list
        Log.d("UpdatedEquipment", "Equipment list updated: " + selectedEquipmentDb.get());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<String> equipmentList = getIntent().getStringArrayListExtra("equipments");
        setContentView(R.layout.my_info_activity);
        currentUser = getIntent().getStringExtra("userId");
        buttonSave = findViewById(R.id.buttonSubmitUpdateInfo);
        // Initialize views
        EditText editTextAge = findViewById(R.id.editTextUpdateAge);
        EditText editTextHeight = findViewById(R.id.editTextUpdateHeight);
        EditText editTextWeight = findViewById(R.id.editTextUpdateWeight);

        Button selectEquipmentButton = findViewById(R.id.equipment_list);
        selectEquipmentButton.setOnClickListener(v -> showGymEquipmentDialog(selectedEquipment -> {
            updateEquipmentList(selectedEquipment, equipmentList);
            for (String equipment : selectedEquipment) {
                Log.d("SelectedEquipment", equipment);
            }
        }));
        buttonSave.setOnClickListener(v -> {
            // Get user input
            String age = editTextAge.getText().toString();
            String weight = editTextWeight.getText().toString();
            String height = editTextHeight.getText().toString();
            updateUserInformationWithEquipment(currentUser, age, weight, height, selectedEquipmentDb.get());
        });
    }





    }

