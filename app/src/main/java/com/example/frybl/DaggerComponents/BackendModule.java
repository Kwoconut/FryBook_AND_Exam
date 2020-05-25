package com.example.frybl.DaggerComponents;

import android.app.Application;

import androidx.room.Room;

import com.example.frybl.LocalDB.AppDatabase;
import com.example.frybl.LocalDB.IngredientDAO;
import com.example.frybl.LocalDB.InstructionDAO;
import com.example.frybl.LocalDB.RecipeDAO;
import com.example.frybl.Repository.AppRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class BackendModule {

    private AppDatabase appDatabase;

    public BackendModule(Application mApplication)
    {
        appDatabase = Room.databaseBuilder(mApplication,AppDatabase.class,"recipe_db").fallbackToDestructiveMigration().build();
    }

    @Singleton
    @Provides
    AppDatabase providesDatabase()
    {
        return appDatabase;
    }

    @Singleton
    @Provides
    AppRepository appRepository(AppDatabase appDatabase)
    {
        return new AppRepository(appDatabase);
    }
}
