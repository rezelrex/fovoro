/**
 * Review
 *
 * This class represents a review object containing details such as description, title, image URL, rated store name, number of likes,
 * and whether the review has been liked by the user. It provides methods to access and manipulate these properties.
 *
 * Properties:
 * - description: A string representing the description or content of the review.
 * - title: A string representing the title or heading of the review.
 * - img: A string representing the URL of the image associated with the review.
 * - ratedStoreName: A string representing the name of the store being reviewed.
 * - key: A string representing the unique identifier key for the review, used for updating the review's like count.
 * - noOfLike: An integer representing the number of likes received for the review.
 * - hasLiked: A boolean indicating whether the review has been liked by the user.
 *
 * Methods:
 * - Review(): Default constructor.
 * - getNoOfLike(): Returns the number of likes for the review.
 * - setHasLiked(boolean hasLiked): Sets whether the review has been liked by the user.
 * - isHasLiked(): Returns true if the review has been liked by the user, otherwise false.
 * - getKey(): Returns the unique identifier key for the review.
 * - setKey(String key): Sets the unique identifier key for the review.
 * - Review(String description, String title, String img, String ratedStoreName, int noOfLike, boolean hasLiked):
 *   Constructor to initialize a Review object with provided values.
 * - getRatedStoreName(): Returns the name of the store being reviewed.
 * - getDescription(): Returns the description or content of the review.
 * - setNoOfLike(int noOfLike): Sets the number of likes for the review.
 * - getTitle(): Returns the title or heading of the review.
 * - getImg(): Returns the URL of the image associated with the review.
 *
 */

package com.example.myapplication.util;

public class Review {
    private String description, title, img, ratedStoreName, key;
    private int noOfLike;
    private boolean hasLiked;

    public Review() {
    }

    public Review(String description, String title, String img, String ratedStoreName, int noOfLike, boolean hasLiked) {
        this.description = description;
        this.title = title;
        this.img = img;
        this.ratedStoreName = ratedStoreName;
        this.noOfLike = noOfLike;
        this.hasLiked = hasLiked;
    }

    public int getNoOfLike() {
        return noOfLike;
    }

    public void setHasLiked(boolean hasLiked) {
        this.hasLiked = hasLiked;
    }

    public boolean isHasLiked() {
        return hasLiked;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRatedStoreName() {
        return ratedStoreName;
    }

    public String getDescription() {
        return description;
    }

    public void setNoOfLike(int noOfLike) {
        this.noOfLike = noOfLike;
    }

    public String getTitle() {
        return title;
    }

    public String getImg() {
        return img;
    }
}
