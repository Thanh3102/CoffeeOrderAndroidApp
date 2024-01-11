package com.example.coffeeorderjavaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignIn extends AppCompatActivity {

    private EditText emailEdt;
    private EditText passwordEdt;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private boolean valid = true;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            redirectToAppropriateActivity(currentUser);
        }
    }
    private void redirectToAppropriateActivity(FirebaseUser user) {
        // Lấy ID người dùng từ Firebase Authentication
        String userId = user.getUid();

        // Truy cập dữ liệu người dùng từ Firestore để xác định vai trò của họ (ví dụ: admin hoặc người dùng thông thường)
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("Users").document(userId);
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    String role = documentSnapshot.getString("role");
                    if ("admin".equals(role)) {
                        Log.e("ACCOUNT ",documentSnapshot.getString("email"));
                        startActivity(new Intent(SignIn.this, AdminActivity.class));
                    } else {
                        Log.e("ACCOUNT ",documentSnapshot.getString("email"));
                        startActivity(new Intent(SignIn.this, MainActivity.class));
                    }
                    finish();
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailEdt = findViewById(R.id.edtEmail);
        passwordEdt = findViewById(R.id.edtPass);
        Button signInBtn = findViewById(R.id.btnSignIn);
        Button signUpBtn = findViewById(R.id.btnSignup);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        signUpBtn.setOnClickListener(view -> {
            startActivity(new Intent(SignIn.this, SignUp.class));
            finish();
        });

        signInBtn.setOnClickListener(v -> {
            String email = emailEdt.getText().toString();
            String password = passwordEdt.getText().toString();

            if (TextUtils.isEmpty(email)) {
                emailEdt.setError("Email is required");
                valid = false;
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEdt.setError("Enter a valid email address");
                valid = false;
            } else {
                emailEdt.setError(null);
            }

            if (TextUtils.isEmpty(password)) {
                passwordEdt.setError("Password is required");
                valid = false;
            } else if (password.length() < 6) {
                passwordEdt.setError("Password must be at least 6 characters");
                valid = false;
            } else {
                passwordEdt.setError(null);
            }

            if (valid) {
                Log.d("@!#!@!@$!@", "onCreate: ");
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                if (firebaseUser != null) {
                                    String userId = firebaseUser.getUid();
                                    DocumentReference userRef = FirebaseFirestore.getInstance().collection("Users").document(userId);
                                    userRef.get().addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            DocumentSnapshot documentSnapshot = task1.getResult();
                                            if (documentSnapshot != null && documentSnapshot.exists()) {
                                                String role = documentSnapshot.getString("role");
                                                if ("admin".equals(role)) {
                                                    Toast.makeText(getApplicationContext(), "Logged in successfully. Welcome to admin page", Toast.LENGTH_SHORT).show();
                                                    Log.d("TAG", "User: " + documentSnapshot.getString("email"));
                                                    startActivity(new Intent(SignIn.this, AdminActivity.class));
                                                    finish();
                                                } else {
                                                    Log.d("TAG", "User: " + documentSnapshot.getString("email"));
                                                    Toast.makeText(getApplicationContext(), "Logged in successfully", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(SignIn.this, MainActivity.class));
                                                    finish();
                                                }
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Failed: " + task1.getException(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                                Log.w("Signin fail", "signin fail: ",task.getException() );

                            }
                        });
            }
        });
    }
}
