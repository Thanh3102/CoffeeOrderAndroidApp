package com.example.coffeeorderjavaapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.coffeeorderjavaapp.R;
import com.example.coffeeorderjavaapp.SignIn;
import com.google.firebase.auth.FirebaseAuth;


public class ProfileFragment extends Fragment {

    private TextView nameTextView;
    private TextView emailTextView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        return rootView;
    }


}