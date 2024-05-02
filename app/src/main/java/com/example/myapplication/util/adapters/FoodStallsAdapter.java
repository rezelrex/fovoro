/**
 * FoodStallsAdapter
 *
 * This adapter is used to populate a GridView with data representing food stalls.
 * It provides custom views for each item in the GridView, including stall name, image, and rating.
 *
 * Dependencies:
 * - Requires a layout file (stall_grid_item.xml) defining the appearance of each grid item.
 *
 * Usage:
 * - Create an instance of FoodStallsAdapter, passing the context, stall names, images, and ratings.
 * - Set the adapter to a GridView using setAdapter() method.
 *
 */

package com.example.myapplication.util.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.myapplication.R;

public class FoodStallsAdapter extends BaseAdapter {

    // Context reference
    private Context context;

    // Data arrays
    private String[] stallName;
    private int[] image;
    private float[] ratings;

    // Layout inflater
    private LayoutInflater inflater;

    /**
     * Constructs a new FoodStallsAdapter with the specified context, stall names, images, and ratings.
     *
     * @param context    The context in which the adapter is being used.
     * @param stallName  An array of stall names.
     * @param image      An array of stall images.
     * @param ratings    An array of stall ratings.
     */
    public FoodStallsAdapter(Context context, String[] stallName, int[] image, float[] ratings) {
        this.context = context;
        this.stallName = stallName;
        this.image = image;
        this.ratings = ratings;
    }

    @Override
    public int getCount() {
        return stallName.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.stall_grid_item, null);
        }

        // Get references to views within the grid item layout
        ImageView imageView = convertView.findViewById(R.id.grid_image);
        TextView textView = convertView.findViewById(R.id.item_name);
        RatingBar ratingBar = convertView.findViewById(R.id.ratingbar);

        // Set data to views
        imageView.setImageResource(image[position]);
        textView.setText(stallName[position]);
        ratingBar.setRating(ratings[position]);

        return convertView;
    }
}
