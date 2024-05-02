/**
 * ImageUploadActivity
 *
 * This activity allows users to upload an image along with a review to Firebase Realtime Database and Storage.
 * Users can select an image from their device's gallery, input a review title and comment, select a food stall from a dropdown list,
 * and rate the stall using a rating bar. Upon clicking the upload button, the image, along with the review details, is uploaded
 * to Firebase Storage, and the corresponding data is stored in Firebase Realtime Database.
 *
 * Layouts:
 * - activity_image_upload.xml: Main layout containing input fields, image view, and upload button.
 *
 * Dependencies:
 * - Firebase Realtime Database for storing review details.
 * - Firebase Storage for storing uploaded images.
 *
 * @author [Your Name]
 * @version 1.0
 */

package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class ImageUploadActivity extends AppCompatActivity {
    private EditText reviewTitle;
    private EditText reviewComment;
    private AutoCompleteTextView reviewStoreName;
    private RatingBar reviewStoreScore;

    ArrayAdapter<String> adapterItems;

    private static final int REQUEST_CODE_IMAGE = 101;
    private ImageView imageViewAdd;
    private EditText inputImageName;
    private TextView textViewProgress;
    private ProgressBar progressBar;
    private Button btnUpload;
    Uri imageUri;
    boolean isImageAdded = false;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    String[] item = {"Chicken Rice", "Indian", "Taiwanese", "Healthy Soup", "Japanese", "Mixed Veg Rice", "Drinks", "Noodles"};
    public String  item_selected = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        // Initialize views
        reviewTitle = findViewById(R.id.review_title);
        reviewComment = findViewById(R.id.review_comment);
        reviewStoreName = findViewById(R.id.auto_complete_txt);
        reviewStoreScore = findViewById(R.id.rate_store_score);
        imageViewAdd = findViewById(R.id.imageViewAdd);
        inputImageName = findViewById(R.id.inputImageName);
        textViewProgress = findViewById(R.id.textViewProgress);
        progressBar = findViewById(R.id.progressBar);
        btnUpload = findViewById(R.id.btn_upload_image);
        textViewProgress.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        // Firebase initialization
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Review");
        storageReference = FirebaseStorage.getInstance().getReference().child("FoodImage");

        // Setup autocomplete dropdown for food stall names
        adapterItems = new ArrayAdapter<>(this, R.layout.activity_toolbar_item, item);
        reviewStoreName.setAdapter(adapterItems);
        reviewStoreName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item_selected = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(ImageUploadActivity.this, "Food Stall: " + item_selected, Toast.LENGTH_SHORT).show();
            }
        });

        // Image selection click listener
        imageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE_IMAGE);
            }
        });

        // Upload button click listener
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String imageName = inputImageName.getText().toString();
                if (isImageAdded && imageName != null) {
                    uploadAll(imageName);
                } else {
                    Toast.makeText(ImageUploadActivity.this, "Please select an image and enter image name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && data != null) {
            imageUri = data.getData();
            isImageAdded = true;
            imageViewAdd.setImageURI(imageUri);
        }
    }

    private void uploadAll(String imageName) {
        textViewProgress.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        final String key = databaseReference.push().getKey();
        storageReference.child(key + ".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.child(key + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("title", reviewTitle.getText().toString());
                        hashMap.put("description", reviewComment.getText().toString());
                        hashMap.put("ratedStoreName", item_selected);
                        hashMap.put("noOfLike", 0);
                        hashMap.put("storeStarsRated", String.valueOf(reviewStoreScore.getRating()));
                        hashMap.put("foodImageName", imageName);
                        hashMap.put("img", uri.toString());
                        hashMap.put("hasLiked", false);

                        databaseReference.child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                startActivity(new Intent(getApplicationContext(), MainPageActivity.class));
                            }
                        });
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (snapshot.getBytesTransferred() * 100) / snapshot.getTotalByteCount();
                progressBar.setProgress((int) progress);
                textViewProgress.setText(progress + " %");
            }
        });
    }
}
