package com.example.coffeeorderjavaapp.fragment;

import static android.companion.CompanionDeviceManager.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeeorderjavaapp.R;
import com.example.coffeeorderjavaapp.SignIn;
import com.example.coffeeorderjavaapp.model.User;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfileFragment extends Fragment {

    private EditText nameEdt, phoneEdt;
    private TextView tvEmail;
    private com.google.android.material.imageview.ShapeableImageView profileImageView;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    StorageReference storageReference;
    LinearProgressIndicator progressIndicator;
    Uri image;

    private Button btnSelectImage, btnLogOut, btnEditProfile;
    String newName, newPhone;
    private boolean valid = true;

    private final ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult o) {
                            if (o.getResultCode() == RESULT_OK) {
                                if (o.getData() != null) {
                                    image = o.getData().getData();
                                    btnEditProfile.setEnabled(true);
                                    // Glide.with(getContext()).load(image).into(profileImageView);
                                    Picasso.with(getContext()).load(image).into(profileImageView);
                                } else {
                                    Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        nameEdt = rootView.findViewById(R.id.tvUsername);
        tvEmail = rootView.findViewById(R.id.tvEmail);
        phoneEdt = rootView.findViewById(R.id.tvPhone);
        btnLogOut = rootView.findViewById(R.id.btnLogOut);
        btnEditProfile = rootView.findViewById(R.id.btnEditProfile);
        profileImageView = rootView.findViewById(R.id.profileImageView);
        btnSelectImage = rootView.findViewById(R.id.btnSelectImage);
        progressIndicator = rootView.findViewById(R.id.progress);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

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

        btnLogOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this.getContext(), SignIn.class);
            startActivity(intent);
        });

        btnEditProfile.setOnClickListener(v -> {
            Log.e("profile", "Button click");
            newName = nameEdt.getText().toString().trim();
            newPhone = phoneEdt.getText().toString().trim();
            if (newName.isEmpty()) {
                Toast.makeText(getContext(), "User name is not empty", Toast.LENGTH_SHORT).show();
                nameEdt.setError("User name is not empty");
                valid = false;
            } else {
                nameEdt.setError(null);
            }
            if (newPhone.isEmpty()) {
                Toast.makeText(getContext(), "Phone is not empty", Toast.LENGTH_SHORT).show();
                phoneEdt.setError("Phone is not empty");
                valid = false;
            } else if (newPhone.length() < 10) {
                Toast.makeText(getContext(), "Phone must be at least 10 characters", Toast.LENGTH_SHORT).show();
                phoneEdt.setError("Phone must be at least 10 characters");
                valid = false;
            } else {
                phoneEdt.setError(null);
                valid = true;
            }

            if (image != null) {
                upLoadImage(image);
            } else {
                Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
            }

            if (currentUser != null && valid) {
                Log.e("edit", "Button click");
                String userId = currentUser.getUid();
                DocumentReference userRef = firestore.collection("Users").document(userId);
                Map<String, Object> updatesUser = new HashMap<>();
                updatesUser.put("username", newName);
                updatesUser.put("phone", newPhone);
                userRef.update(updatesUser)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getContext(), "User information updated successfully!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getContext(), "Failed to update user information!", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            activityResultLauncher.launch(intent);
        });

        return rootView;
    }

    private void displayUserInfo(User user) {
        nameEdt.setText(user.getUsername());
        tvEmail.setText(user.getEmail());
        phoneEdt.setText(user.getPhone());

        // Load hình ảnh từ URL bằng thư viện hình ảnh như Picasso
        if (user.getImageUser() != null && !user.getImageUser().isEmpty()) {
            Picasso.with(requireContext()).load(user.getImageUser()).into(profileImageView);
        }
    }

    private void upLoadImage(Uri image) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DocumentReference userRef = firestore.collection("Users").document(userId);

            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        User user = document.toObject(User.class);
                        if (user != null && user.getImageUser() != null && !user.getImageUser().isEmpty()) {
                            StorageReference oldImageRef = FirebaseStorage.getInstance().getReferenceFromUrl(user.getImageUser());
                            oldImageRef.delete().addOnSuccessListener(aVoid -> uploadNewImage(image))
                                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to delete old image!", Toast.LENGTH_SHORT).show());
                        } else {
                            uploadNewImage(image);
                        }
                    }
                }
            });
        }
    }

    private void uploadNewImage(Uri image) {
        String userId = firebaseAuth.getCurrentUser().getUid();
        StorageReference reference = storageReference.child("users/" + userId + "/image/" + UUID.randomUUID().toString());

        btnEditProfile.setEnabled(false);

        reference.putFile(image)
                .addOnSuccessListener(taskSnapshot -> {
                    reference.getDownloadUrl().addOnSuccessListener(uri -> updateUserImage(uri.toString()));
                    Toast.makeText(getContext(), "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Image upload failed!", Toast.LENGTH_SHORT).show())
                .addOnProgressListener(snapshot -> {
                    int progress = (int) ((100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount());
                    progressIndicator.setProgress(progress);
                    if (progress == 100) {
                        btnEditProfile.setEnabled(true);
                        progressIndicator.setProgress(0);
                    }
                });
    }

    private void updateUserImage(String imageUrl) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DocumentReference userRef = firestore.collection("Users").document(userId);
            userRef.update("imageUser", imageUrl)
                    .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Image updated successfully!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Image update failed!", Toast.LENGTH_SHORT).show());
        }
    }
}
