/**
 * Food
 *
 * This class represents a food item, including its image name and URL.
 * It is typically used to store data related to food items in the application.
 *
 * Usage:
 * - Create instances of the Food class to represent individual food items.
 * - Set the image name and URL using the appropriate setter methods.
 * - Retrieve the image name and URL using the getter methods.
 *
 */

package com.example.myapplication.util;

public class Food {
    // Image name of the food item (key name from imageUploadActivity)
    private String foodImageName;

    // URL of the food item image
    private String img;

    /**
     * Constructs a new Food object with the specified image name and URL.
     *
     * @param foodImageName The image name of the food item.
     * @param img           The URL of the food item image.
     */
    public Food(String foodImageName, String img) {
        this.foodImageName = foodImageName;
        this.img = img;
    }

    /**
     * Default constructor for Food class.
     * Needed for Firebase deserialization.
     */
    public Food() {
        // Default constructor
    }

    /**
     * Get the image name of the food item.
     *
     * @return The image name of the food item.
     */
    public String getFoodImageName() {
        return foodImageName;
    }

    /**
     * Get the URL of the food item image.
     *
     * @return The URL of the food item image.
     */
    public String getImg() {
        return img;
    }
}
