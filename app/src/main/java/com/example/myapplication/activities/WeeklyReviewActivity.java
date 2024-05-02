/**
 * WeeklyReviewActivity
 *
 * This activity displays a list of reviews sorted by the number of likes (rated score).
 * It retrieves data from the Firebase Realtime Database and populates a RecyclerView with the reviews.
 *
 * Dependencies:
 * - Firebase Realtime Database for storing and retrieving reviews
 * - RecyclerView and associated adapters for displaying the list of reviews
 *
 * Layout:
 * - activity_weekly_review.xml
 *
 * Usage:
 * - Ensure that the layout activity_weekly_review.xml is properly configured with a RecyclerView
 *   with the ID "myReviewRecyclerView".
 *
 */

package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.util.Review;
import com.example.myapplication.util.adapters.ReviewRecyclerViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class WeeklyReviewActivity extends AppCompatActivity {

    // Firebase Database references
    private DatabaseReference databaseReference, likeReference;

    // RecyclerView and adapter
    private RecyclerView recyclerView;
    private ReviewRecyclerViewAdapter myAdapter;

    // Data list for reviews
    private ArrayList<Review> reviewModels;

    // Database instance
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_review);

        // Initialize Firebase Database references
        databaseReference = FirebaseDatabase.getInstance().getReference("Review");
        likeReference = FirebaseDatabase.getInstance().getReference("UserLikes");

        // Initialize RecyclerView and adapter
        recyclerView = findViewById(R.id.myReviewRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewModels = new ArrayList<>();
        myAdapter = new ReviewRecyclerViewAdapter(this, reviewModels);
        recyclerView.setAdapter(myAdapter);

        // Add ValueEventListener to fetch data from the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the review list to avoid duplication
                reviewModels.clear();

                // Iterate through each "Review" node in the database
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Convert data to Review object using getValue method
                    Review review = snapshot.getValue(Review.class);
                    if (review != null) {
                        review.setKey(snapshot.getKey());
                        reviewModels.add(review);
                    }
                }

                // Sort the reviews based on the number of likes (rated score)
                Collections.sort(reviewModels, new Comparator<Review>() {
                    @Override
                    public int compare(Review review1, Review review2) {
                        return Integer.compare(review2.getNoOfLike(), review1.getNoOfLike());
                    }
                });

                // Notify the adapter of the data change
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors that occur during data retrieval
                // For example, logging errors or displaying error messages
            }
        });
    }
}
