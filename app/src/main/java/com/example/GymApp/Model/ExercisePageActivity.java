package com.example.GymApp.Model;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;


import androidx.appcompat.app.AppCompatActivity;

import com.example.GymApp.R;

import java.util.Locale;

public class ExercisePageActivity extends AppCompatActivity {

    private String exerciseName;
    private ProgressBar timerProgressBar;
    private CountDownTimer countDownTimer;
    private TextView timerTextView;


    private int exercisePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_page_layout);

        // Initialize views
        ImageView backBtn = findViewById(R.id.back_btn);
        Button startExerciseBtn = findViewById(R.id.start_exercise_btn);
        TextView exerciseNameTextView = findViewById(R.id.exercise_name);
        ImageView exercisePhotoImageView = findViewById(R.id.exercise_image);
        timerTextView = findViewById(R.id.timerTextView);


        // Get the exerciseName and exercisePhoto from the intent extras
        Intent intent = getIntent();
        if (intent != null) {
            exerciseName = intent.getStringExtra("exerciseName");
            exercisePhoto = intent.getIntExtra("exercisePhoto", 0); // Default value 0 if not found

            // Display the exerciseName in the TextView
            exerciseNameTextView.setText(exerciseName);

            // Load the GIF into the ImageView using Glide
            if (exercisePhoto != 0) {
                Glide.with(this)
                        .asGif()
                        .load(exercisePhoto)
                        .into(exercisePhotoImageView);
            }
        }

        // Set click listener for back button
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the activity and go back to the previous one
            }
        });

        startExerciseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                startTimer(60000); // 60 seconds
                Toast.makeText(ExercisePageActivity.this, "60 Sec Started", Toast.LENGTH_SHORT).show();

            }
        });

        // Set click listener for start exercise button
        timerProgressBar = findViewById(R.id.timerProgressBar);
        timerProgressBar.setProgress(0);


//        startExerciseBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (countDownTimer != null) {
//                    countDownTimer.cancel();
//                }
//                startTimer(60000); // 60 seconds
//                Toast.makeText(ExercisePageActivity.this, "60 Sec Started", Toast.LENGTH_SHORT).show();
//
//            }
//        });
    }
    private void startTimer(long timeInMilliseconds) {
        timerProgressBar.setMax((int) timeInMilliseconds / 1000);
        countDownTimer = new CountDownTimer(timeInMilliseconds, 1000) {
            public void onTick(long millisUntilFinished) {
                int secondsLeft = (int) (millisUntilFinished / 1000);
                timerProgressBar.setProgress(timerProgressBar.getMax() - secondsLeft);
                timerTextView.setText(String.valueOf(secondsLeft));
            }

            public void onFinish() {
                timerProgressBar.setProgress(timerProgressBar.getMax());
                timerTextView.setText("0");
                Toast.makeText(ExercisePageActivity.this, "Exercise Completed!", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    private void startTimerold(long timeInMilliseconds) {
        timerProgressBar.setMax((int) timeInMilliseconds / 1000);
        countDownTimer = new CountDownTimer(timeInMilliseconds, 1000) {
            public void onTick(long millisUntilFinished) {
                int progress = (int) (millisUntilFinished / 1000);
                timerProgressBar.setProgress(timerProgressBar.getMax() - progress);
            }

            public void onFinish() {
                timerProgressBar.setProgress(timerProgressBar.getMax());
                Toast.makeText(ExercisePageActivity.this, "Exercise Completed!", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }
}