package com.example.android.recipedetective.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {FavouritesObject.class}, version = 1, exportSchema = false)
public abstract class FavouritesDatabase extends RoomDatabase {
    public abstract FavouritesDao favouritesDao();
    private static FavouritesDatabase INSTANCE;

    public static FavouritesDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FavouritesDatabase.class) {
                if (INSTANCE == null) {

                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FavouritesDatabase.class, "favourites_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
