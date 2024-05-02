/**
 * MyProfileActivity allows users to view and upload their profile image.
 * Users can select an image from their device's gallery to upload as their profile image.
 * The uploaded image is stored in Firebase Storage, and the image URL is stored in Firebase Realtime Database.
 */
package com.example.myapplication.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class MyProfileActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_IMAGE = 101;
    private TextView profile_name;
    private ImageView profile_image;
    private Button btnUploadProfileImage;
    private FirebaseAuth firebaseAuth;

    DatabaseReference databaseReference;
    StorageReference storageReference;
    Uri imageUri;
    boolean isImageAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        // Initialize Firebase components
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("ProfileImage");
        storageReference = FirebaseStorage.getInstance().getReference().child("ProfileImageStore");

        // Find views
        profile_name = findViewById(R.id.profile_name);
        profile_image = findViewById(R.id.iv_profile_image);
        btnUploadProfileImage = findViewById(R.id.btn_upload_profile_image);

        // Load user information
        getUserInfo();

        // Set onClickListener for selecting profile image
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open gallery to select image
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE_IMAGE);
            }
        });

        // Set onClickListener for uploading profile image
        btnUploadProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get profile name and upload image if image is added
                final String imageName = profile_name.getText().toString();
                if (isImageAdded && imageName != null) {
                    uploadImage(imageName);
                } else {
                    Toast.makeText(MyProfileActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Load user information
    private void getUserInfo() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String emailNo = currentUser.getEmail();
            String userName = emailNo.substring(0, emailNo.indexOf("@")).replace("_", "");
            if (userName != null && !userName.isEmpty()) {
                profile_name.setText(userName);
            }
        }
    }

    // Upload image to Firebase Storage and image URL to Firebase Realtime Database
    private void uploadImage(String imageName) {
        final String key = databaseReference.push().getKey();
        storageReference.child(key + ".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.child(key + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("ImageUrl", uri.toString());
                        databaseReference.child(imageName).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(MyProfileActivity.this, "Your profile image uploaded successfully!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainPageActivity.class));
                            }
                        });
                    }
                });
            }
        });
    }

    // Handle result of selecting image from gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Set image URI and flag indicating image is added
            imageUri = data.getData();
            isImageAdded = true;
            profile_image.setImageURI(imageUri);
        }
    }
}
