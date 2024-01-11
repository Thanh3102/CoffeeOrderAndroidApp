package com.example.coffeeorderjavaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeeorderjavaapp.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;

    TextView loginbtn;
    Button signupbtn;
    EditText emailEdt, passwordEdt, nameEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        loginbtn = findViewById(R.id.tvLogin);
        signupbtn = findViewById(R.id.btnSignup);
        emailEdt = findViewById(R.id.edtEmail);
        passwordEdt = findViewById(R.id.edtPass);
        nameEdt = findViewById(R.id.edtName);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        loginbtn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), SignIn.class));
            finish();
        });

        signupbtn.setOnClickListener(v -> {
            if (validateFields()) {
                firebaseAuth.createUserWithEmailAndPassword(emailEdt.getText().toString(), passwordEdt.getText().toString())
                        .addOnSuccessListener(authResult -> {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null) {
                                Toast.makeText(getApplicationContext(), "Account Created", Toast.LENGTH_SHORT).show();
                                String userId = user.getUid();
                                DocumentReference userRef = firestore.collection("Users").document(userId);
                                User userInfo = new User(nameEdt.getText().toString(), emailEdt.getText().toString(), passwordEdt.getText().toString(), "user");
                                userRef.set(userInfo);

                                Intent i = new Intent(getApplicationContext(), SignIn.class);
                                startActivity(i);
                                finish();
                            }
                        })
                        .addOnFailureListener(e -> {
                            // Xử lý lỗi và hiển thị thông báo cho người dùng
                            String errorMessage = e.getMessage();
                            if (errorMessage != null) {
                                if (errorMessage.contains("email address is already in use")) {
                                    Toast.makeText(getApplicationContext(), "Email address is already in use.", Toast.LENGTH_SHORT).show();
                                } else if (errorMessage.contains("address is badly formatted")) {
                                    Toast.makeText(getApplicationContext(), "Invalid email address format.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Failed to create account: " + errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Failed to create account. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private boolean validateFields() {
        boolean valid = true;

        if (nameEdt.getText().toString().isEmpty()) {
            nameEdt.setError("Enter your name");
            Toast.makeText(getApplicationContext(), "Enter your name", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            nameEdt.setError(null);
        }

        if (emailEdt.getText().toString().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailEdt.getText().toString()).matches()) {
            emailEdt.setError("Enter a valid email address");
            Toast.makeText(getApplicationContext(), "Enter a valid email address", Toast.LENGTH_SHORT).show();

            valid = false;
        } else {
            emailEdt.setError(null);
        }

        if (passwordEdt.getText().toString().isEmpty() || passwordEdt.getText().toString().length() < 6) {
            passwordEdt.setError("Password must be at least 6 characters");
            Toast.makeText(getApplicationContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            passwordEdt.setError(null);
        }



        return valid;
    }
}
