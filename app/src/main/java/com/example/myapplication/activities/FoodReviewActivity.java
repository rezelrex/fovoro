/**
 * FoodReviewActivity
 *
 * This activity serves as a placeholder for displaying food reviews.
 * It simply inflates the layout activity_food_review.xml.
 *
 * Layout:
 * - activity_food_review.xml
 *
 * Usage:
 * - This activity can be used to display food reviews by inflating the appropriate layout.
 * - Additional functionality and content can be added as needed.
 *
 */

package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.myapplication.R;

public class FoodReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_review);
    }
}
