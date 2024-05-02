/**
 * ReviewRecyclerViewAdapter
 *
 * This adapter is used to populate a RecyclerView with review data.
 * It provides custom views for each item in the RecyclerView, including title, description,
 * image, like count, and store name.
 * Users can also like or dislike a review, with the like count updating accordingly.
 *
 * Dependencies:
 * - Glide library for loading images efficiently.
 * - Firebase Realtime Database for storing and updating review data.
 *
 * Usage:
 * - Create an instance of ReviewRecyclerViewAdapter, passing the context and a list of Review objects.
 * - Set the adapter to a RecyclerView using setAdapter() method.
 *
 * Example:
 * ArrayList<Review> reviewList = new ArrayList<>();
 * // Populate reviewList with Review objects
 * ReviewRecyclerViewAdapter adapter = new ReviewRecyclerViewAdapter(context, reviewList);
 * recyclerView.setAdapter(adapter);
 *
 * @author [Your Name]
 * @version 1.0
 */

package com.example.myapplication.util.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.util.Review;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.MyViewHolder> {

    Context context; // Context for inflating the layout
    ArrayList<Review> reviewModels; // ArrayList to hold the review models

    /**
     * Constructs a new ReviewRecyclerViewAdapter with the specified context and review models.
     *
     * @param context      The context in which the adapter is being used.
     * @param reviewModels ArrayList of Review objects representing the review data.
     */
    public ReviewRecyclerViewAdapter(Context context, ArrayList<Review> reviewModels) {
        this.context = context;
        this.reviewModels = reviewModels;
    }

    @NonNull
    @Override
    public ReviewRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout for each row
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.weekly_review_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewRecyclerViewAdapter.MyViewHolder holder, int position) {
        // Set values to views created in weekly_review_item layout file
        // Update data for each row based on the position in the RecyclerView

        Review review = reviewModels.get(position);
        holder.tvTitle.setText(review.getTitle());
        holder.tvDescription.setText(review.getDescription());
        holder.tvStoreName.setText(review.getRatedStoreName());
        Glide.with(context).load(review.getImg()).into(holder.tvImageView);
        holder.tvLikeCount.setText(String.valueOf(review.getNoOfLike()));

        // Check if the user has liked the review and update the like button accordingly
        if (review.isHasLiked()) {
            holder.tvLikeButton.setImageResource(R.drawable.ic_like);
        } else {
            holder.tvLikeButton.setImageResource(R.drawable.ic_dislike);
        }

        // Set click listener for the like button to handle like/unlike functionality
        holder.tvLikeButton.setOnClickListener(v -> {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Review").child(review.getKey());
            databaseReference.child("hasLiked").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    Boolean hasLiked = dataSnapshot.getValue(Boolean.class);
                    if (hasLiked == null || !hasLiked) {
                        // Increment like count and set hasLiked to true if not already liked
                        int newScore = review.getNoOfLike() + 1;
                        review.setNoOfLike(newScore);
                        review.setHasLiked(true);
                        holder.tvLikeCount.setText(String.valueOf(newScore));
                        holder.tvLikeButton.setImageResource(R.drawable.ic_like);
                        databaseReference.child("noOfLike").setValue(newScore);
                        databaseReference.child("hasLiked").setValue(true);
                    } else {
                        // Decrement like count and set hasLiked to false if already liked
                        int newScore = Math.max(0, review.getNoOfLike() - 1); // Prevent negative like count
                        review.setNoOfLike(newScore);
                        review.setHasLiked(false);
                        holder.tvLikeCount.setText(String.valueOf(newScore));
                        holder.tvLikeButton.setImageResource(R.drawable.ic_dislike);
                        databaseReference.child("noOfLike").setValue(newScore);
                        databaseReference.child("hasLiked").setValue(false);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Firebase", "Error getting data", e);
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        // Return total number of items to be displayed
        return reviewModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView tvImageView;
        private TextView tvTitle, tvDescription, tvLikeCount, tvStoreName;
        private ImageButton tvLikeButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views from the weekly_review_item layout file
            tvImageView = itemView.findViewById(R.id.recyclerImage);
            tvTitle = itemView.findViewById(R.id.ImageTitle);
            tvDescription = itemView.findViewById(R.id.ImageComment);
            tvLikeCount = itemView.findViewById(R.id.likeCount);
            tvStoreName = itemView.findViewById(R.id.StoreName);
            tvLikeButton = itemView.findViewById(R.id.like);
        }
    }
}
