/**
 * MainPageActivity displays the main page of the application, including a slide menu for navigation.
 * Users can access various features of the application from this page, such as weekly reviews, food stalls, submitting reviews, and their profile.
 * The user's information and profile image are fetched from Firebase and displayed on the page.
 * Users can log out from this page by clicking on the logout button.
 */
package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.util.SlideMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// Side menu page
public class MainPageActivity extends AppCompatActivity {

    // Declare variables
    private FirebaseAuth firebaseAuth;
    private ImageView mIvHead;
    private TextView mTvUser;
    private SlideMenu slideMenu;
    private Button mBtnWeeklyReview, mBtnStoreReview, mBtnFoodReview,
            mBtnTopdownList, to_weekly_review, to_food_stalls,
            to_submit_reviews, to_my_profile;

    FloatingActionButton floatingBtn_logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_slide);

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Find views
        mTvUser = findViewById(R.id.user_name);
        mIvHead = findViewById(R.id.iv_head);
        slideMenu = findViewById(R.id.slideMenu);
        mBtnWeeklyReview = findViewById(R.id.btn_main_weekly_review);
        mBtnStoreReview = findViewById(R.id.btn_main_food_store);
        mBtnFoodReview = findViewById(R.id.btn_main_my_profile);
        mBtnTopdownList = findViewById(R.id.btn_main_submit_review);
        to_weekly_review = findViewById(R.id.to_weekly_review);
        to_food_stalls = findViewById(R.id.to_food_stalls);
        to_submit_reviews = findViewById(R.id.to_submit_review);
        floatingBtn_logOut = findViewById(R.id.logOut);
        to_my_profile = findViewById(R.id.to_my_profile);

        // Load user information and profile image
        getUserInfo();

        // Set onClickListener for profile image to toggle slide menu
        mIvHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideMenu.switchMenu();
            }
        });

        // Set onClickListener for buttons
        setListener();
    }

    // Set onClickListener for buttons
    private void setListener(){
        onClick onClick = new onClick();
        mBtnWeeklyReview.setOnClickListener(onClick);
        mBtnStoreReview.setOnClickListener(onClick);
        mBtnFoodReview.setOnClickListener(onClick);
        mBtnTopdownList.setOnClickListener(onClick);
        floatingBtn_logOut.setOnClickListener(onClick);

        to_weekly_review.setOnClickListener(onClick);
        to_food_stalls.setOnClickListener(onClick);
        to_submit_reviews.setOnClickListener(onClick);
        to_my_profile.setOnClickListener(onClick);
    }

    // Get user information and profile image from Firebase
    private void getUserInfo() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String emailNo = currentUser.getEmail();
            String userFormat = emailNo.substring(0,emailNo.indexOf("@")).replace("_","");
            String userName = "Hi, "+ userFormat +"."+"\nWelcome Back!";
            if (userName != null && !userName.isEmpty()) {
                mTvUser.setText(userName);
            }

            DatabaseReference profileImageRef = FirebaseDatabase.getInstance().getReference().child("ProfileImage").child(userFormat);
            profileImageRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String imageUrl = dataSnapshot.child("ImageUrl").getValue(String.class);
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Glide.with(MainPageActivity.this).load(imageUrl).into(mIvHead);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MainPageActivity.this, "Failed to load profile image.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // OnClickListener for button actions
    private class onClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.btn_main_weekly_review:
                    intent = new Intent(MainPageActivity.this, WeeklyReviewActivity.class);
                    break;
                case R.id.btn_main_food_store:
                    intent = new Intent(MainPageActivity.this, FoodStallsActivity.class);
                    break;
                case R.id.btn_main_my_profile:
                    intent = new Intent(MainPageActivity.this, MyProfileActivity.class);
                    break;
                case R.id.btn_main_submit_review:
                    intent = new Intent(MainPageActivity.this, ImageUploadActivity.class);
                    break;


                case R.id.to_weekly_review:
                    intent = new Intent(MainPageActivity.this, WeeklyReviewActivity.class);
                    break;
                case R.id.to_food_stalls:
                    intent = new Intent(MainPageActivity.this, FoodStallsActivity.class);
                    break;
                case R.id.to_submit_review:
                    intent = new Intent(MainPageActivity.this, ImageUploadActivity.class);
                    break;
                case R.id.to_my_profile:
                    intent = new Intent(MainPageActivity.this, MyProfileActivity.class);
                    break;
                case R.id.logOut:
                    // Log out the user
                    firebaseAuth.signOut();
                    intent = new Intent(MainPageActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
            if(intent != null)
                startActivity(intent);
        }
    }
}

