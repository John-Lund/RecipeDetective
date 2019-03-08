package com.example.android.recipedetective.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;


@Dao
public interface FavouritesDao {

    @Query("SELECT * FROM favourites_table ORDER BY recipeName ASC")
    LiveData<List<FavouritesObject>> getFavourites();

    @Insert
    void insertFavourite(FavouritesObject favouritesObject);

    @Delete
    void deleteFavourite(FavouritesObject favouritesObject);

}
