package com.example.android.recipedetective.search;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.android.recipedetective.R;
import com.example.android.recipedetective.model.SearchObject;
import com.example.android.recipedetective.repo.Repository;

import java.util.List;

public class SearchActivityViewModel extends AndroidViewModel {

    private Application mApplication;
    private Repository mRepository;
    private MutableLiveData<List<SearchObject>> mSearchResults;

    public SearchActivityViewModel(@NonNull Application application) {
        super(application);
        this.mRepository = Repository.getRepository();
        this.mSearchResults = mRepository.getSearchResults();
        this.mApplication = application;
    }

    public MutableLiveData<List<SearchObject>> getSearchResults() {
        return mSearchResults;
    }

    public String processSearchString(String string) {
        string = string.trim();
        if (string.isEmpty()) {
            return string;
        }
        string = string.toLowerCase();
        string = string.replaceAll("( )+", " ");
        string = string.replaceAll("(,)+", ",");
        string = string.replaceAll("( ,)+", "");
        string = string.replaceAll("(, )+", ",");
        if (string.indexOf(",") == 0) {
            string = string.substring(1);
        }
        if (string.lastIndexOf(",") == string.length() - 1) {
            string = string.substring(0, string.length() - 1);
        }
        if (!string.matches("[a-zA-Z|,( )]+")) {
            return mApplication.getResources().getString(R.string.search_activity_invalid_characters_warning);
        }

        string = string.replaceAll(" ", "+");

        if (!string.contains(",")) {
            String[] searchWords = {string};
            mRepository.refreshOnlineData(searchWords);
            return string;
        } else {
            String[] searchWords = string.split(",");
            mRepository.refreshOnlineData(searchWords);
            return string;
        }

    }

    public void setCurrentSearchObject(int recipeIndex) {
        mRepository.setCurrentSearchObject(recipeIndex);
    }

    public void upDateCurrentDetailsObject() {
        mRepository.updateDetailsObject();
    }
}
