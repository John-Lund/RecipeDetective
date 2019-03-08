package com.example.android.recipedetective.model;

public class WidgetDataObject {
    private String recipeTitle;
    private String basicIngredientsList;

    public WidgetDataObject(String recipeTitle, String basicIngredientsList) {
        this.recipeTitle = recipeTitle;
        this.basicIngredientsList = basicIngredientsList;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public String getBasicIngredientsList() {
        return basicIngredientsList;
    }

}
