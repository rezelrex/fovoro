/**
 * FoodStallsActivity
 *
 * FoodStallsActivity is an activity that displays a list of food stalls with their respective images and ratings.
 * Users can sort the list by ratings or alphabetically and click on a stall to view its menu.
 *
 * Features:
 * - Display a grid of food stalls with images and ratings.
 * - Sort the stalls by ratings or alphabetically.
 * - Click on a stall to view its menu.
 *
 * Usage:
 * - Include FoodStallsActivity in your AndroidManifest.xml file.
 * - Set up the layout XML file for the activity, including the necessary views.
 * - Use the FoodStallsAdapter to populate the grid view with data.
 * - Implement sorting functionality for the stalls list.
 * - Handle click events to navigate to the menu activity when a stall is selected.
 *
 */

package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityFoodStallsBinding;
import com.example.myapplication.util.adapters.FoodStallsAdapter;

public class FoodStallsActivity extends AppCompatActivity {
    public static final String KEY = "MyKey";
    ActivityFoodStallsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_stalls);
        binding = ActivityFoodStallsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the sort button
        Button sortButton = findViewById(R.id.sortButton);

        // Data for food stalls
        String[] stallName = {"Chicken Rice", "Indian", "Taiwanese", "Healthy Soup", "Japanese", "Mixed Veg Rice", "Drinks", "Noodles"};
        int[] stallImages = {R.drawable.chickenrice, R.drawable.indian, R.drawable.taiwanese, R.drawable.healthysoup, R.drawable.japanese, R.drawable.mixedrice, R.drawable.drinks, R.drawable.noodles};
        float[] ratings = {2.5f, 4f, 3.5f, 3.5f, 3f, 4f, 5f, 2.5f};

        // Initialize the adapter for the grid view
        FoodStallsAdapter gridAdapter = new FoodStallsAdapter(FoodStallsActivity.this, stallName, stallImages, ratings);
        binding.gridView.setAdapter(gridAdapter);

        final int[] counter = {0};

        // Sort button click listener
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter[0]++;
                if (counter[0] % 2 != 0) {
                    // Sort by ratings
                    for (int i = ratings.length - 1; i > 0; i--) {
                        for (int j = 0; j < i; j++) {
                            if (ratings[j] < ratings[j + 1]) {
                                // Swap elements
                                float temp = ratings[j];
                                ratings[j] = ratings[j + 1];
                                ratings[j + 1] = temp;

                                String tempName = stallName[j];
                                stallName[j] = stallName[j + 1];
                                stallName[j + 1] = tempName;

                                int tempImage = stallImages[j];
                                stallImages[j] = stallImages[j + 1];
                                stallImages[j + 1] = tempImage;
                            }
                        }
                    }
                    // Update the grid view with sorted data
                    FoodStallsAdapter gridAdapter = new FoodStallsAdapter(FoodStallsActivity.this, stallName, stallImages, ratings);
                    binding.gridView.setAdapter(gridAdapter);
                    Toast.makeText(FoodStallsActivity.this, "Sorted By: Ratings", Toast.LENGTH_SHORT).show();

                } else {
                    // Sort alphabetically
                    // Reset data to original order
                    String[] stallName = {"Chicken Rice", "Indian", "Taiwanese", "Healthy Soup", "Japanese", "Mixed Veg Rice", "Drinks", "Noodles"};
                    int[] stallImages = {R.drawable.chickenrice, R.drawable.indian, R.drawable.taiwanese, R.drawable.healthysoup, R.drawable.japanese, R.drawable.mixedrice, R.drawable.drinks, R.drawable.noodles};
                    float[] ratings = {2.5f, 4f, 3.5f, 3.5f, 3f, 4f, 5f, 2.5f};
                    // Update the grid view with original data
                    FoodStallsAdapter gridAdapter = new FoodStallsAdapter(FoodStallsActivity.this, stallName, stallImages, ratings);
                    binding.gridView.setAdapter(gridAdapter);
                    Toast.makeText(FoodStallsActivity.this, "Sorted By: Alphabet(A-Z)", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Grid view item click listener
        binding.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Show toast message with selected stall name
                Toast.makeText(FoodStallsActivity.this, "You Clicked on " + stallName[i], Toast.LENGTH_SHORT).show();
                // Start MenuActivity and pass selected stall name as extra data
                Intent intent = new Intent(FoodStallsActivity.this, MenuActivity.class);
                intent.putExtra(KEY, stallName[i]);
                startActivity(intent);
            }
        });
    }
}
