package com.example.android.recipedetective.database;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "favourites_table")
public class FavouritesObject {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String recipeId;
    private String recipeName;
    private String sourceName;
    private String sourceRecipeUrl;
    private String servings;
    private String totalTime;
    private String detailedIngredientsList;
    private String basicIngredientsList;
    private String imageUrl;

    @Ignore
    public FavouritesObject(String recipeId,
                            String recipeName,
                            String sourceName,
                            String sourceRecipeUrl,
                            String servings,
                            String totalTime,
                            String detailedIngredientsList,
                            String basicIngredientsList,
                            String imageUrl) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.sourceName = sourceName;
        this.sourceRecipeUrl = sourceRecipeUrl;
        this.servings = servings;
        this.totalTime = totalTime;
        this.detailedIngredientsList = detailedIngredientsList;
        this.basicIngredientsList = basicIngredientsList;
        this.imageUrl = imageUrl;
    }
    public FavouritesObject(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceRecipeUrl() {
        return sourceRecipeUrl;
    }

    public void setSourceRecipeUrl(String sourceRecipeUrl) {
        this.sourceRecipeUrl = sourceRecipeUrl;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getDetailedIngredientsList() {
        return detailedIngredientsList;
    }

    public void setDetailedIngredientsList(String detailedIngredientsList) {
        this.detailedIngredientsList = detailedIngredientsList;
    }

    public String getBasicIngredientsList() {
        return basicIngredientsList;
    }

    public void setBasicIngredientsList(String basicIngredientsList) {
        this.basicIngredientsList = basicIngredientsList;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
