package com.example.android.recipedetective.reviews;

import android.arch.lifecycle.ViewModel;

import com.example.android.recipedetective.database.FavouritesObject;
import com.example.android.recipedetective.repo.Repository;

public class WriteReviewActivityViewModel extends ViewModel {
    private Repository mRepository;

    public WriteReviewActivityViewModel() {
        this.mRepository = Repository.getRepository();
    }
    public FavouritesObject getCurrentFavouritesObject(){
        return mRepository.getCurrentFavouritesObject();
    }
}
