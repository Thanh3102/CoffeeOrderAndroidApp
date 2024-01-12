package com.example.coffeeorderjavaapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.coffeeorderjavaapp.R;
import com.example.coffeeorderjavaapp.SignIn;
import com.example.coffeeorderjavaapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.ZoneId;


public class ProfileFragment extends Fragment {

    private EditText nameEdt, emailEdt, phoneEdt;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        nameEdt = rootView.findViewById(R.id.tvUsername);
        emailEdt = rootView.findViewById(R.id.tvEmail);
        phoneEdt = rootView.findViewById(R.id.tvPhone);
        Button logoutBtn = rootView.findViewById(R.id.btnLogOut);
        Button edtProfile = rootView.findViewById(R.id.btnEditProfile);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Lấy thông tin người dùng từ Firestore
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DocumentReference userRef = firestore.collection("Users").document(userId);
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        User user = document.toObject(User.class);
                        if (user != null) {
                            displayUserInfo(user);
                        }
                    }
                }
            });
        }

        logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this.getContext(), SignIn.class);
            startActivity(intent);
        });

        return rootView;
    }
    private void displayUserInfo(User user) {
        nameEdt.setText(user.getUsername());
        emailEdt.setText(user.getEmail());
        phoneEdt.setText(user.getPhone());

        // Load hình ảnh từ URL bằng thư viện hình ảnh như Picasso hoặc Glide
        // Picasso.get().load(user.getImage()).into(profileImageView);
        // Glide.with(this).load(user.getImage()).into(profileImageView);
    }
}