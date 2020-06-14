package com.example.frybl.Utility.DaggerComponents;

import android.app.Application;

import androidx.room.Room;

import com.example.frybl.LocalDB.AppDatabase;
import com.example.frybl.LocalDB.DAO.IngredientDAO;
import com.example.frybl.LocalDB.DAO.InstructionDAO;
import com.example.frybl.LocalDB.DAO.RecipeDAO;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LocalDBModule {

    private AppDatabase appDatabase;

    public LocalDBModule(Application mApplication)
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
    IngredientDAO providesIngredientDAO(){
        return appDatabase.ingredientDAO();
    }

    @Singleton
    @Provides
    InstructionDAO providesInstructionDAO()
    {
        return appDatabase.instructionDAO();
    }

    @Singleton
    @Provides
    RecipeDAO providesRecipeDAO()
    {
        return appDatabase.recipeDAO();
    }

}
