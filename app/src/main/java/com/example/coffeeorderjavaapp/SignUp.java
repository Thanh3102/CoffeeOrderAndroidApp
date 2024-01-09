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

    boolean valid = true;
    TextView loginbtn;
    Button signupbtn;
    EditText emailEdt,passwordEdt,nameEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        loginbtn =findViewById(R.id.tvLogin);
        signupbtn = findViewById(R.id.btnSignup);
        emailEdt = findViewById(R.id.edtEmail);
        passwordEdt  = findViewById(R.id.edtPass);
        nameEdt = findViewById(R.id.edtName);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        loginbtn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), SignIn.class));
            finish();
        });

        signupbtn.setOnClickListener(v -> {
            checkField(emailEdt);
            checkField(passwordEdt);
            checkField(nameEdt);

            if (valid){
                firebaseAuth.createUserWithEmailAndPassword(emailEdt.getText().toString(),passwordEdt.getText().toString()).addOnSuccessListener(authResult -> {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    Toast.makeText(getApplicationContext(),"Account Created",Toast.LENGTH_SHORT).show();
                    DocumentReference df = firestore.collection("Users").document(user.getUid());
                    User userInfo = new User(nameEdt.getText().toString(),emailEdt.getText().toString(),passwordEdt.getText().toString(),"user");
                    df.set(userInfo);

                    Intent i = new Intent(getApplicationContext(), SignIn.class);
                    startActivity(i);
                    finish();

                }).addOnFailureListener( e -> {
                    Toast.makeText(getApplicationContext(),"Fail to created account",Toast.LENGTH_SHORT).show();

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