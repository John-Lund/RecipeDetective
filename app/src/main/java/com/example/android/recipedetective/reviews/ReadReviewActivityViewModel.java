package com.example.android.recipedetective.reviews;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.recipedetective.database.FavouritesObject;
import com.example.android.recipedetective.model.ReviewObject;
import com.example.android.recipedetective.repo.Repository;

import java.util.List;

public class ReadReviewActivityViewModel extends ViewModel {
    private Repository mRepository;

    public ReadReviewActivityViewModel() {
        this.mRepository = Repository.getRepository();
    }

    public LiveData<List<FavouritesObject>> getFavouritesList() {
        return mRepository.getFavouritesLiveData();
    }

    public void createFavourite(ReviewObject reviewObject) {
        FavouritesObject favouritesObject = new FavouritesObject(
                reviewObject.getRecipeId(),
                reviewObject.getRecipeTitle(),
                reviewObject.getSourceDisplayName(),
                reviewObject.getSourceRecipeUrl(),
                reviewObject.getServings(),
                reviewObject.getPreparationTime(),
                reviewObject.getDetailedIngredients(),
                reviewObject.getBasicIngredientsList(),
                reviewObject.getImageUrl());
        mRepository.addFavouriteToDatabase(favouritesObject);
    }
}
