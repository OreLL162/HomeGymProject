package com.example.GymApp.Model;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.GymApp.Data.StayHealthyTip;
import android.Manifest;
import com.example.GymApp.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class HomeScreenActivity extends AppCompatActivity {
    // Variables to hold user information
    private static final int PERMISSION_REQUEST_CODE = 100;
    private String userId;
    private String email;
    private String age;
    private String name;
    private String weight;
    private String height;
    private List<String> equipment;
    private static Boolean isLogin = false;

    // UI elements
    private CalendarView calendarView;
    private Button showTodayButton;
    private static ProgressBar progressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen_activity);
        // Initialize UI elements
        calendarView = findViewById(R.id.calendarView);
        showTodayButton = findViewById(R.id.BMICalculator);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress(0); // Set the progress to a fixed value
        progressBar.setMax(100);
        // Set minimum date to today's date
        calendarView.setMinDate(Calendar.getInstance().getTimeInMillis());
        // Check if the user is logged in and show a random tip if not already done
        if (!isLogin) {
            showRandomTip();
        }
        isLogin = true;
        // Set click listener for "Show Today" button
        showTodayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set CalendarView's date to today's date
                calendarView.setDate(Calendar.getInstance().getTimeInMillis(), true, true);

            }
        });

        // Set listener for CalendarView to schedule training session
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Show a confirmation dialog to the user
            new AlertDialog.Builder(HomeScreenActivity.this)
                    .setTitle("Schedule Training")
                    .setMessage("Do you want to schedule a training session for this date?")
                    .setPositiveButton("OK", (dialog, which) -> {
                        // Schedule the training session
                        scheduleTraining(year, month, dayOfMonth);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });


        // Retrieve userId from intent extras
        userId = getIntent().getStringExtra("userUid");
        if (userId != null && !userId.isEmpty()) {
            loadUserData(userId);
        } else {
            Log.d(TAG, "UserId is null or empty");
            // Handle case where userId is not passed correctly
        }

        // Set click listener for "Manage Info" button
        Button btnManageInfo = findViewById(R.id.btn_manage_info);
        if (btnManageInfo != null) {
            btnManageInfo.setOnClickListener(view -> {
                // Create an intent to start MyInfoActivity
                Intent infoIntent = new Intent(HomeScreenActivity.this, MyInfoActivity.class);
                infoIntent.putExtra("userId", userId);
                infoIntent.putStringArrayListExtra("equipments", (ArrayList<String>) equipment);
                startActivity(infoIntent);
            });
        } else {
            Log.d(TAG, "Manage Info Button not found...");
        }

        // Set click listener for "Manage Exercises" button
        Button btnManageExercises = findViewById(R.id.btn_your_exercises);
        if (btnManageExercises != null) {
            btnManageExercises.setOnClickListener(view -> {
                // Create an intent to start ManageExercisesActivity
                Intent manageExercisesIntent = new Intent(HomeScreenActivity.this, ManageExercisesActivity.class);
                manageExercisesIntent.putStringArrayListExtra("equipments", (ArrayList<String>) equipment);
                manageExercisesIntent.putExtra("userId", userId);
                startActivity(manageExercisesIntent);
            });
        } else {
            Log.d(TAG, "Manage Exercises Button not found...");
        }
        Button showTodayButton = findViewById(R.id.BMICalculator);
        showTodayButton.setOnClickListener(v -> {
            // Set CalendarView's date to today's date
            // Show the tip in a dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Your BMI Result:" + calculateBMI(weight, height));
            builder.setMessage(classifyBMI(calculateBMI(weight, height)));
            builder.setPositiveButton("OK", null);
            builder.show();

        });
    }

    public static String classifyBMI(double bmi) {
        try {

        if (bmi < 18.5) {
            return "You are underweight. It's important to eat a balanced diet and consult a healthcare provider if necessary.";
        } else if (bmi >= 18.5 && bmi <= 24.9) {
            return "You have a normal weight. Great job maintaining a healthy body weight!";
        } else if (bmi >= 25 && bmi <= 29.9) {
            return "You are overweight. Consider diet and exercise to reach a healthier body weight.";
        } else if (bmi >= 30) {
            return "You are obese. It's crucial to seek advice from a healthcare provider to manage your health.";
        } else {
            return "Invalid BMI. Please check the input values.";
        }
        } catch (NullPointerException e) {
            return "Error: " + e.getMessage();
        }
    }
    public static Double calculateBMI(String weightKgStr, String heightCmStr) {
        double weightKg, heightCm;

        try {
            // Parse weight and height from strings to doubles
            weightKg = Double.parseDouble(weightKgStr);
            heightCm = Double.parseDouble(heightCmStr);

            // Check for valid values
            if (heightCm <= 0 || weightKg <= 0) {
                System.out.println("Error: Weight and height must be greater than zero.");
                return null;
            }

            // Convert height from centimeters to meters
            double heightM = heightCm / 100.0;

            // Calculate and return BMI
            return weightKg / (heightM * heightM);
        } catch (NumberFormatException |NullPointerException e) {
            System.out.println("Error: Invalid number format. " + e.getMessage());
            return null;
        }
    }

    // Method to load user data from Firestore
    @SuppressLint("RestrictedApi")
    public void loadUserData(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(userId);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    // Update UI with user information
                    updateUserInterface(document);
                    name = document.getString("name");
                    email = document.getString("email");
                    weight = document.getString("weight");
                    height = document.getString("height");
                    equipment = (List<String>) document.get("equipment");
                    TextView welcomeTitle = findViewById(R.id.HomeScreenTitle);
                    welcomeTitle.setText("Welcome Back " + name + " !");
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    // Method to update UI with user information
    private void updateUserInterface(DocumentSnapshot document) {
        age = document.getString("age");
        weight = document.getString("weight");
        email = document.getString("email");
        height = document.getString("height");
        equipment = (List<String>) document.get("equipment");
    }

    // Method to show a random health tip
    private void showRandomTip() {
        Random random = new Random();
        StayHealthyTip stayHealthyTips = null; // Replace null with your object instance
        StayHealthyTip randomTip = stayHealthyTips.tips.get(random.nextInt(stayHealthyTips.tips.size()));
        String tipText = randomTip.getTip();

        // Show the tip in a dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Stay Healthy Tip");
        builder.setMessage(tipText);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    // Method to schedule training session on the selected date
    private void scheduleTraining(int year, int month, int dayOfMonth) {
        // Check if we have the WRITE_CALENDAR permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, PERMISSION_REQUEST_CODE);
        } else {
            // Permission has already been granted, proceed with scheduling
            insertEvent(year, month, dayOfMonth);
        }
    }

    // Method to insert the event into the calendar
    // Method to insert the event into the calendar
    private void insertEvent(int year, int month, int dayOfMonth) {
        checkCalendarPermission();
        if (!isEventAlreadyScheduled(year, month, dayOfMonth)) {
            // Get a Calendar instance and set the selected date
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, dayOfMonth);


            // Get the ContentResolver
            ContentResolver cr = getContentResolver();

            // Create a ContentValues object to hold the event details
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, cal.getTimeInMillis());
            values.put(CalendarContract.Events.DTEND, cal.getTimeInMillis() + 3600000); // End time (1 hour later)
            values.put(CalendarContract.Events.TITLE, "Training Session");
            values.put(CalendarContract.Events.DESCRIPTION, "Scheduled training session");
            values.put(CalendarContract.Events.CALENDAR_ID, 1); // Ensure this is a valid Calendar ID
            values.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance().getTimeZone().getID());

            // Insert the event into the calendar
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            if (uri != null) {
                // Update progress bar by 5%
                updateProgressBar(5);
                Toast.makeText(this, "Training scheduled successfully", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Event already scheduled for this time.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isEventAlreadyScheduled(int year, int month, int dayOfMonth) {
        Calendar startOfDay = Calendar.getInstance();
        startOfDay.set(year, month, dayOfMonth, 0, 0, 0); // Set to beginning of the day
        long startTime = startOfDay.getTimeInMillis();

        Calendar endOfDay = Calendar.getInstance();
        endOfDay.set(year, month, dayOfMonth, 23, 59, 59); // Set to end of the day
        long endTime = endOfDay.getTimeInMillis();

        Cursor cursor = null;
        ContentResolver cr = getContentResolver();

        // Define the URI and the columns to retrieve
        Uri uri = CalendarContract.Events.CONTENT_URI;
        String[] projection = new String[]{
                CalendarContract.Events._ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DTEND
        };

        // Define the selection criteria
        String selection = "(" + CalendarContract.Events.DTSTART + " >= ?) AND ("
                + CalendarContract.Events.DTEND + " <= ?) AND ("
                + CalendarContract.Events.TITLE + " = ?)";

        // Define the selection arguments (pass the start time, end time, and title)
        String[] selectionArgs = new String[]{
                String.valueOf(startTime),
                String.valueOf(endTime),
                "Training Session"
        };

        // Query the content provider
        try {
            cursor = cr.query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                return true; // Event already exists on this day
            }
        } catch (Exception e) {
            Log.e(TAG, "Error querying calendar", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return false;
    }


    // Method to update progress bar by specified percentage
    private void updateProgressBar(int percentage) {
        int currentProgress = progressBar.getProgress();
        int newProgress = currentProgress + percentage;
         // Set the maximum value to 100 for easy percentage calculation
        progressBar.setProgress(newProgress);
    }



    private void checkCalendarPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CALENDAR) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CALENDAR)) {

                new AlertDialog.Builder(this)
                        .setTitle("Permission Needed")
                        .setMessage("This app needs the Calendar permissions to manage your training sessions.")
                        .setPositiveButton("OK", (dialog, which) -> requestCalendarPermissions())
                        .create()
                        .show();
            } else {
                // No explanation needed; request the permission
                requestCalendarPermissions();
            }
        } else {
            // Permission has already been granted
        }
    }

    private void requestCalendarPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR},
                PERMISSION_REQUEST_CODE);
    }


    // Handle the permissions result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions are granted. Continue with calendar operations

            } else {
                // Permissions are denied. Show an explanatory message or disable functionality
                Toast.makeText(this, "Permission denied. Cannot schedule training without calendar access.", Toast.LENGTH_LONG).show();
            }
        }
    }




}
