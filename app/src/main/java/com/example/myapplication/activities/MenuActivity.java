/**
 * MenuActivity displays the menu options based on the type of food selected.
 * It dynamically sets the layout based on the value received from the FoodStallsActivity.
 * Users can view the menu options for different types of food.
 */
package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class MenuActivity extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String value = intent.getStringExtra(FoodStallsActivity.KEY);
        // Set the layout based on the type of food selected
        if (value.equals("Chicken Rice")) {
            setContentView(R.layout.activity_chicken_rice_menu);
        } else if (value.equals("Indian")) {
            setContentView(R.layout.activity_indian_menu);
        } else if (value.equals("Taiwanese")) {
            setContentView(R.layout.activity_taiwanese_menu);
        } else if (value.equals("Healthy Soup")) {
            setContentView(R.layout.activity_healthy_soup_menu);
        } else if (value.equals("Japanese")) {
            setContentView(R.layout.activity_japanese_menu);
        } else if (value.equals("Caifan")) {
            setContentView(R.layout.activity_mixed_rice_menu);
        } else if (value.equals("Drinks")) {
            setContentView(R.layout.activity_drinks_menu);
        } else if (value.equals("Noodles")) {
            setContentView(R.layout.activity_noodles_menu);
        }

        // Find the return button
        button = findViewById(R.id.return_button);

        // Set onClickListener for the return button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Close the MenuActivity
                finish();
            }
        });
    }
}
