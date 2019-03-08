package com.example.android.recipedetective;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import com.example.android.recipedetective.repo.Repository;

public class MainActivityViewModel extends ViewModel {
    private Repository mRepository;
    public MainActivityViewModel() {
        this.mRepository = Repository.getRepository();
    }

    public void setUpDatabase(Application application){
    mRepository.setUpDatabase(application);
    }
    public void clearCurrentSearchResults(){
        mRepository.clearCurrentSearchResults();
    }
}
