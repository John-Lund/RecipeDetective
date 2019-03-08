package com.example.android.recipedetective.search;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.recipedetective.database.FavouritesObject;
import com.example.android.recipedetective.model.DetailsObject;
import com.example.android.recipedetective.model.SearchObject;
import com.example.android.recipedetective.repo.Repository;

import java.util.List;

public class SearchDetailsViewModel extends ViewModel {
    private Repository mRepository;

    public SearchDetailsViewModel() {
        this.mRepository = Repository.getRepository();
    }

    public MutableLiveData<DetailsObject> getDetails() {
        return mRepository.getDetailsLiveData();
    }

    public SearchObject getCurrentSearchObject() {
        return mRepository.getCurrentSearchObject();
    }

    public void createFavourite() {
        SearchObject searchObject = mRepository.getCurrentSearchObject();
        DetailsObject detailsObject = mRepository.getCurrentDetailsObject();
        FavouritesObject favouritesObject = new FavouritesObject(
                searchObject.getRecipeId(),
                searchObject.getRecipeName(),
                searchObject.getSourceDisplayName(),
                detailsObject.getSourceRecipeUrl(),
                detailsObject.getServings(),
                detailsObject.getPreparationTime(),
                detailsObject.getDetailedIngredients(),
                searchObject.getIngredientsList(),
                searchObject.getImagineUrlString());
        mRepository.addFavouriteToDatabase(favouritesObject);
    }

    public LiveData<List<FavouritesObject>> getFavouritesList() {
        return mRepository.getFavouritesLiveData();
    }
}
