package com.example.coffeeorderjavaapp;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SignIn extends AppCompatActivity {

    private EditText emailEdt;
    private EditText passwordEdt;
    private FirebaseAuth firebaseAuth;
    private boolean valid = true;
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
//        if(currentUser != null){
//            redirectToAppropriateActivity(currentUser);
//        }
//    }
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
                        Log.e("ACCOUNT ", Objects.requireNonNull(documentSnapshot.getString("email")));
                        Log.e("Redirect", "redirectToAppropriateActivity: Admin" );
                        Toast.makeText(getApplicationContext(), "WELCOME TO ADMIN", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignIn.this, AdminActivity.class));
                    } else {
                        Log.e("Redirect", "redirectToAppropriateActivity: User" );
                        Log.e("ACCOUNT ", Objects.requireNonNull(documentSnapshot.getString("email")));
                        Toast.makeText(getApplicationContext(), "WELCOME TO USER", Toast.LENGTH_SHORT).show();
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

        signUpBtn.setOnClickListener(view -> {
            startActivity(new Intent(SignIn.this, SignUp.class));
            finish();
        });

        signInBtn.setOnClickListener(v -> {
            String email = emailEdt.getText().toString().trim();
            String password = passwordEdt.getText().toString().trim();

            Log.e("sign in", "Button click" );

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Email is required", Toast.LENGTH_SHORT).show();
                emailEdt.setError("Email is required");
                valid = false;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getApplicationContext(), "Enter a valid email address", Toast.LENGTH_SHORT).show();
                emailEdt.setError("Enter a valid email address");
                valid = false;
            } else {
                emailEdt.setError(null);
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(), "Password is required", Toast.LENGTH_SHORT).show();
                passwordEdt.setError("Password is required");
                valid = false;
            } else if (password.length() < 6) {
                Toast.makeText(getApplicationContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
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
                                    redirectToAppropriateActivity(firebaseUser);
                                }
                            }
                        }).addOnFailureListener(e -> {
                            String errorMessage = e.getMessage();
                            if(errorMessage.contains("A network error")){
                                Toast.makeText(getApplicationContext(), "SIGN IN FAIL NETWORK ERROR", Toast.LENGTH_SHORT).show();
                                Log.w("NETWORK ERROR", "signin fail: "+ errorMessage);

                            }else {
                            Toast.makeText(getApplicationContext(), "SIGN IN FAIL auth credential is incorrect", Toast.LENGTH_SHORT).show();
                            Log.w("Signin fail", "signin fail: "+ errorMessage);}
                        });
            }
        });
    }
}
