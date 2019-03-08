package com.example.android.recipedetective.favourites;

import android.arch.lifecycle.ViewModel;

import com.example.android.recipedetective.database.FavouritesObject;
import com.example.android.recipedetective.repo.Repository;

public class FavouritesDetailsViewModel extends ViewModel {
    private Repository mRepository;

    public FavouritesDetailsViewModel() {
        this.mRepository = Repository.getRepository();
    }

    public FavouritesObject getCurrentFavouritesObject() {
        return mRepository.getCurrentFavouritesObject();
    }

    public void deleteCurrentFavouritesObject() {
        mRepository.deleteCurrentFavouritesObject();
    }

    public void setCurrentWidgetDataObject(String recipeTitle, String baicIngredientsList) {
        mRepository.setCurrentWidgetDataObject(recipeTitle, baicIngredientsList);
    }
}
