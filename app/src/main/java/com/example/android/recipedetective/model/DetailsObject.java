package com.example.android.recipedetective.model;

public class DetailsObject {
    private  String mRecipeId;
    private  String mRecipeName;
    private  String mSourceName;
    private  String mSourceRecipeUrl;
    private  String mServings;
    private  String mPreparationTime;
    private  String mDetailedIngredients;

    public DetailsObject(String mRecipeId, String mRecipeName, String mSourceName, String mSourceRecipeUrl, String mServings, String mPreparationTime, String mDetailedIngredients) {
        this.mRecipeId = mRecipeId;
        this.mRecipeName = mRecipeName;
        this.mSourceName = mSourceName;
        this.mSourceRecipeUrl = mSourceRecipeUrl;
        this.mServings = mServings;
        this.mPreparationTime = mPreparationTime;
        this.mDetailedIngredients = mDetailedIngredients;
    }

    public String getRecipeId() {
        return mRecipeId;
    }

    public String getRecipeName() {
        return mRecipeName;
    }

    public String getSourceName() {
        return mSourceName;
    }

    public String getSourceRecipeUrl() {
        return mSourceRecipeUrl;
    }

    public String getServings() {
        return mServings;
    }

    public String getPreparationTime() {
        return mPreparationTime;
    }

    public String getDetailedIngredients() {
        return mDetailedIngredients;
    }




}
