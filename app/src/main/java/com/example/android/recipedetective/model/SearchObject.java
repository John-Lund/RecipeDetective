package com.example.android.recipedetective.model;

public class SearchObject {
    private String mImagineUrlString;
    private String mSourceDisplayName;
    private String mIngredientsList;
    private String mRecipeName;
    private String mRecipeId;
    private int mRating;
    private int mRatingReference;
    public SearchObject(String mImagineUrlString, String mSourceDisplayName, String mIngredientsList, String mRecipeName, String mRecipeId, int mRating, int mRatingReference) {
        this.mImagineUrlString = mImagineUrlString;
        this.mSourceDisplayName = mSourceDisplayName;
        this.mIngredientsList = mIngredientsList;
        this.mRecipeName = mRecipeName;
        this.mRecipeId = mRecipeId;
        this.mRating = mRating;
        this.mRatingReference = mRatingReference;
    }

    public String getImagineUrlString() {
        return mImagineUrlString;
    }

    public String getSourceDisplayName() {
        return mSourceDisplayName;
    }

    public String getIngredientsList() {
        return mIngredientsList;
    }

    public String getRecipeName() {
        return mRecipeName;
    }

    public String getRecipeId() {
        return mRecipeId;
    }

    public int getmRating() {
        return mRating;
    }

    public int getmRatingReference() {
        return mRatingReference;
    }
}
