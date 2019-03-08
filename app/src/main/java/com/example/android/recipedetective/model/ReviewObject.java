package com.example.android.recipedetective.model;

public class ReviewObject {
    private String recipeTitle;
    private String basicIngredientsList;
    private String imageUrl;
    private String author;
    private String recipeId;
    private String reviewText;
    private String headerValue;
    private String footerValue;
    private int userRating;
    private int reviewStarsImageId;
    private boolean recipeIsFavourite;
    private String sourceDisplayName;
    private String sourceRecipeUrl;
    private String servings;
    private String preparationTime;
    private String detailedIngredients;

    public ReviewObject(String recipeTitle,
                        String basicIngredientsList,
                        String imageUrl,
                        String author,
                        String recipeId,
                        String reviewText,
                        String headerValue,
                        int userRating,
                        String footerValue,
                        String sourceDisplayName,
                        String sourceRecipeUrl,
                        String servings,
                        String preparationTime,
                        String detailedIngredients) {
        this.recipeTitle = recipeTitle;
        this.basicIngredientsList = basicIngredientsList;
        this.imageUrl = imageUrl;
        this.author = author;
        this.recipeId = recipeId;
        this.reviewText = reviewText;
        this.headerValue = headerValue;
        this.userRating = userRating;
        this.footerValue = footerValue;

        this.sourceDisplayName = sourceDisplayName;
        this.sourceRecipeUrl = sourceRecipeUrl;
        this.servings = servings;
        this.preparationTime = preparationTime;
        this.detailedIngredients = detailedIngredients;
    }
    public ReviewObject() {
    }

    public String getSourceDisplayName() {
        return sourceDisplayName;
    }

    public String getSourceRecipeUrl() {
        return sourceRecipeUrl;
    }

    public String getServings() {
        return servings;
    }

    public String getPreparationTime() {
        return preparationTime;
    }

    public String getDetailedIngredients() {
        return detailedIngredients;
    }

    public boolean getRecipeIsFavourite() {
        return recipeIsFavourite;
    }

    public void setRecipeIsFavourite(boolean recipeIsFavourite) {
        this.recipeIsFavourite = recipeIsFavourite;
    }

    public int getReviewStarsImageId() {
        return reviewStarsImageId;
    }

    public void setReviewStarsImageId(int reviewStarsImageId) {
        this.reviewStarsImageId = reviewStarsImageId;
    }

    public String getFooterValue() {
        return footerValue;
    }

    public void setFooterValue(String footerValue) {
        this.footerValue = footerValue;
    }

    public int getUserRating() {
        return userRating;
    }

    public String getHeaderValue() {
        return headerValue;
    }

    public void setHeaderValue(String headerValue) {
        this.headerValue = headerValue;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public String getBasicIngredientsList() {
        return basicIngredientsList;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAuthor() {
        return author;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public String getReviewText() {
        return reviewText;
    }

}
