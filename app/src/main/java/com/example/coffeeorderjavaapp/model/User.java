package com.example.coffeeorderjavaapp.model;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class User {

    private String username;
    private String email;
    private String password;
    private String role;

    public User() {
    }

    public User(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

//    public static  boolean isAdmin(String email, String password){
//        // Lấy tài khoản người dùng từ Firestore Database
//        DocumentReference df = FirebaseFirestore.getInstance().collection("users").document(email);
//        DocumentSnapshot documentSnapshot = df.get().getResult();
//        if (documentSnapshot == null){
//            return false;
//        }
//        String role = documentSnapshot.getString("role");
//        if (role.equals("admin")){
//            return true;
//        }
//        return false;
//    }
}
