package com.example.android.recipedetective.favourites;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.recipedetective.database.FavouritesObject;
import com.example.android.recipedetective.repo.Repository;

import java.util.List;

public class FavouritesActivityViewModel extends ViewModel {
    private Repository mRepository;
    private List<FavouritesObject> mFavouritesList;

    public FavouritesActivityViewModel() {
        this.mRepository = Repository.getRepository();
    }

    public LiveData<List<FavouritesObject>> getFavouritesLiveData() {
        return mRepository.getFavouritesLiveData();
    }

    public void setCurrentFavouritesObject(int index) {
        mRepository.setCurrentFavouritesObject(mFavouritesList.get(index));
    }

    public void setFavouritesList(List<FavouritesObject> favouritesList) {
        mFavouritesList = favouritesList;
    }
}
