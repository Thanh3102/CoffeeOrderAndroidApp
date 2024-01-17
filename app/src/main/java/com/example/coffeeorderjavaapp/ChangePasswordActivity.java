package com.example.coffeeorderjavaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText edtOldPassword, edtNewPassword;
    Button btnChangePassword , btnChangePasswordBack;
    private FirebaseAuth firebaseAuth;
    String oldPassword,newPassword;
    private boolean valid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtOldPassword = findViewById(R.id.edtOldPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnChangePasswordBack = findViewById(R.id.btnChangePasswordBack);
        firebaseAuth = FirebaseAuth.getInstance();

        btnChangePasswordBack.setOnClickListener(v -> {
            finish();
        });

        btnChangePassword.setOnClickListener(v -> {
            oldPassword = edtOldPassword.getText().toString();
            newPassword = edtNewPassword.getText().toString();
            if (TextUtils.isEmpty(oldPassword)) {
                Toast.makeText(getApplicationContext(), "Vui lòng nhập mật khẩu cũ", Toast.LENGTH_SHORT).show();
                edtOldPassword.setError("Vui lòng nhập mật khẩu cũ");
                valid = false;

            } else if (oldPassword.length() < 6) {
                Toast.makeText(getApplicationContext(), "Mật khẩu cũ phải trên 6 kí tự", Toast.LENGTH_SHORT).show();
                edtOldPassword.setError("Mật khẩu cũ phải trên 6 kí tự");
                valid =false;

            } else {
                edtOldPassword.setError(null);
                valid = true;
            }

            if (TextUtils.isEmpty(newPassword)) {
                Toast.makeText(getApplicationContext(), "Vui lòng nhập mật khẩu mới", Toast.LENGTH_SHORT).show();
                edtOldPassword.setError("Vui lòng nhập mật khẩu mới");
                valid = false;

            } else if (oldPassword.length() < 6) {
                Toast.makeText(getApplicationContext(), "Mật khẩu mới phải trên 6 kí tự", Toast.LENGTH_SHORT).show();
                edtOldPassword.setError("Mật khẩu mới phải trên 6 kí tự");
                valid =false;

            } else {
                edtNewPassword.setError(null);
                valid = true;
            }
            if(valid){
                handleChangePassword();
            }

        });
    }

    private void handleChangePassword() {




        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {

            String email = user.getEmail();

            AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword);

            user.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            user.updatePassword(newPassword)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            updateFirestore(user);
                                            Toast.makeText(ChangePasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(ChangePasswordActivity.this, "Failed to change password", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, "Old password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }
    private void updateFirestore(FirebaseUser user) {

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String userId = user.getUid();
        DocumentReference userRef = firestore.collection("Users").document(userId);


        userRef.update("password", newPassword).addOnSuccessListener(unused -> {
            Log.d("Pass", "Password updated in Firestore");

        }).addOnFailureListener(e -> {
            Log.e("Pass", "Error updating password in Firestore", e);
        });

    }
}