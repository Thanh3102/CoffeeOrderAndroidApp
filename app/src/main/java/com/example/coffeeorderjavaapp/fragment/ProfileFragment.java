package com.example.coffeeorderjavaapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.coffeeorderjavaapp.R;
import com.example.coffeeorderjavaapp.SignIn;
import com.google.firebase.auth.FirebaseAuth;
public class ProfileFragment extends Fragment {

    private TextView nameTextView;
    private TextView emailTextView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        nameTextView = rootView.findViewById(R.id.profileUsername);
        Button logoutBtn = rootView.findViewById(R.id.logoutBtn);

        nameTextView.setText(FirebaseAuth.getInstance().getCurrentUser().getUid());

        logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this.getContext(), SignIn.class);
            startActivity(intent);
        });

        return rootView;
    }
}