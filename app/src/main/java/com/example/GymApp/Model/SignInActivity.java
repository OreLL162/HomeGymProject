package com.example.GymApp.Model;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.GymApp.MainActivity;
import com.example.GymApp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_activity);

        mAuth = FirebaseAuth.getInstance();

        EditText editTextUsername = findViewById(R.id.editTextUsername);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonSignIn);
        Button buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(v -> {
            Intent i = new Intent(SignInActivity.this,SignUpActivity.class);
            startActivity(i);
        });

        buttonLogin.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            // Checks if either field is empty
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(SignInActivity.this, "Username and password cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(SignInActivity.this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SignInActivity.this, "Sign in Successful", Toast.LENGTH_SHORT).show();

                            // Proceed to HomeScreenActivity and pass user UID as an extra
                            Intent intent = new Intent(SignInActivity.this, HomeScreenActivity.class);
                            intent.putExtra("userUid", mAuth.getUid()); // Pass user UID as an extra
                            startActivity(intent);
                            finish(); // Call this to finish the current activity
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignInActivity.this, "Sign in failed. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    });
        });
    }
}