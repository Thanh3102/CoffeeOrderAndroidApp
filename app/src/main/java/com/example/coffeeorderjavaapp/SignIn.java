package com.example.coffeeorderjavaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignIn extends AppCompatActivity {
    private String email;
    private String password;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    boolean valid = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        EditText emailEdt = findViewById(R.id.edtEmail);
        EditText passwordEdt  = findViewById(R.id.edtPass);
        Button signInBtn = findViewById(R.id.btnSignIn);
        Button signUpBtn = findViewById(R.id.btnSignup);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        signUpBtn.setOnClickListener(view -> {
            Intent i  = new Intent(getApplicationContext(), SignUp.class);
            startActivity(i);
            finish();
        });

        signInBtn.setOnClickListener(v -> {
            checkField(emailEdt);
            checkField(passwordEdt);
            email = emailEdt.getText().toString();
            password = passwordEdt.getText().toString();
            if(valid){
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this,task -> {
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        // Lấy ID của người dùng từ Firebase Authentication
                        String userId = firebaseUser.getUid();

                        // Lấy thông tin người dùng từ Firestore
                        DocumentReference userRef = FirebaseFirestore.getInstance().collection("Users").document(userId);
                        userRef.get().addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                String role = documentSnapshot.getString("role");
                                if (role != null && role.equals("admin")) {
                                    Toast.makeText(getApplicationContext(), "Log in successfully. Welcome to admin page", Toast.LENGTH_SHORT).show();
                                    Intent i  = new Intent(getApplicationContext(), AdminActivity.class);
                                    startActivity(i);
                                    finish();
                                }else {
                                    Toast.makeText(getApplicationContext(), "Log in successfully", Toast.LENGTH_SHORT).show();
                                    Intent i  = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        }).addOnFailureListener(e -> {
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                        });
                    }
                });
            }
        });
    }
    public boolean checkField(EditText txt){
        if(txt.getText().toString().isEmpty()){
            txt.setError("Error");
            valid = false;
        }else {
            valid = true;
        }
        return valid;
    }


}