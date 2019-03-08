package com.example.android.recipedetective.repo;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.example.android.recipedetective.database.FavouritesDao;
import com.example.android.recipedetective.database.FavouritesDatabase;
import com.example.android.recipedetective.database.FavouritesObject;
import com.example.android.recipedetective.model.DetailsObject;
import com.example.android.recipedetective.model.SearchObject;
import com.example.android.recipedetective.model.WidgetDataObject;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {

    private static Repository INSTANCE;
    private static MutableLiveData<List<SearchObject>> mSearchResults = new MutableLiveData<>();
    private static List<SearchObject> mCurrentSearchResults;
    private static MutableLiveData<DetailsObject> mRecipeDetails = new MutableLiveData<>();
    private static DetailsObject mCurrentDetailsObject = null;
    private LiveData<List<FavouritesObject>> mFavouritesListLiveData;
    private FavouritesObject mCurrentFavouritesObject;
    private SearchObject mCurrentSearchObject;
    private FavouritesDatabase mFavouritesDatabase;
    private FavouritesDao mFavouritesDao;
    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();
    private WidgetDataObject mCurrentWidgetDataObject;

    private Repository() {
    }

    public static Repository getRepository() {
        if (INSTANCE == null) {
            synchronized (Repository.class) {
                INSTANCE = new Repository();
            }
        }
        return INSTANCE;
    }

    // search data loading methods
    public void refreshOnlineData(String[] searchWords) {
        new LoadRecipeData(searchWords).execute();
    }

    public MutableLiveData<List<SearchObject>> getSearchResults() {
        return mSearchResults;
    }

    public List<SearchObject> getCurrentSearchResults() {
        return mCurrentSearchResults;
    }

    public void clearCurrentSearchResults() {
        mSearchResults.setValue(null);

    }

    //// Details loading methods
    public void updateDetailsObject() {
        new LoadRecipeDetailsData(mCurrentSearchObject.getRecipeId()).execute();
    }

    public MutableLiveData<DetailsObject> getDetailsLiveData() {
        return mRecipeDetails;
    }

    public DetailsObject getCurrentDetailsObject() {
        return mCurrentDetailsObject;
    }

    public SearchObject getCurrentSearchObject() {
        return mCurrentSearchObject;
    }

    public void setCurrentSearchObject(int recipeIndex) {
        mCurrentSearchObject = mCurrentSearchResults.get(recipeIndex);

    }

    ///////////////////////////////////////////////////////////////////
    //                      database methods
    ///////////////////////////////////////////////////////////////////
    public void setUpDatabase(Application application) {
        mFavouritesDatabase = FavouritesDatabase.getDatabase(application);
        mFavouritesDao = mFavouritesDatabase.favouritesDao();
        mFavouritesListLiveData = mFavouritesDao.getFavourites();
    }

    ///// insert Favourite method ////////////////////////////////
    public void addFavouriteToDatabase(FavouritesObject favouritesObject) {
        mExecutorService.execute(new InsertFavourite(favouritesObject));

    }

    /// general access methods
    public LiveData<List<FavouritesObject>> getFavouritesLiveData() {

        return mFavouritesListLiveData;
    }

    public FavouritesObject getCurrentFavouritesObject() {
        return mCurrentFavouritesObject;
    }

    public void setCurrentFavouritesObject(FavouritesObject favouritesObject) {
        mCurrentFavouritesObject = favouritesObject;
    }

    // delete Favourite method
    public void deleteCurrentFavouritesObject() {
        mExecutorService.execute(new DeleteFavourite(mCurrentFavouritesObject));

    }

    ////////////////////////////// widget methods
    public WidgetDataObject getCurrentWidgetDataObject() {
        return mCurrentWidgetDataObject;
    }

    public void setCurrentWidgetDataObject(String recipeTitle, String basicIngredientsList) {
        mCurrentWidgetDataObject = new WidgetDataObject(recipeTitle, basicIngredientsList);
    }

    private static class LoadRecipeData extends AsyncTask<Void, Void, List<SearchObject>> {
        private String[] mSearchWords;

        private LoadRecipeData(String[] searchWords) {
            this.mSearchWords = searchWords;
        }

        @Override
        protected List<SearchObject> doInBackground(Void... voids) {
            return Utilities.getRecipes(mSearchWords);
        }

        @Override
        protected void onPostExecute(List<SearchObject> recipes) {
            mSearchResults.setValue(recipes);
            mCurrentSearchResults = recipes;
        }
    }

    private static class LoadRecipeDetailsData extends AsyncTask<Void, Void, DetailsObject> {
        private String mRecipeId;

        private LoadRecipeDetailsData(String recipeId) {
            this.mRecipeId = recipeId;
        }

        @Override
        protected DetailsObject doInBackground(Void... voids) {
            return Utilities.getRecipeDetails(mRecipeId);
        }

        @Override
        protected void onPostExecute(DetailsObject detailsObject) {
            mCurrentDetailsObject = detailsObject;
            mRecipeDetails.setValue(detailsObject);
        }
    }

    private class InsertFavourite implements Runnable {
        private FavouritesObject favouritesObject;

        private InsertFavourite(FavouritesObject favouritesObject) {
            this.favouritesObject = favouritesObject;
        }

        @Override
        public void run() {
            mFavouritesDao.insertFavourite(favouritesObject);
        }
    }

    private class DeleteFavourite implements Runnable {
        private FavouritesObject favouritesObject;

        private DeleteFavourite(FavouritesObject favouritesObject) {
            this.favouritesObject = favouritesObject;
        }

        @Override
        public void run() {
            mFavouritesDao.deleteFavourite(favouritesObject);
        }
    }
}

