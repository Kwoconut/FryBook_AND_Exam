package com.example.frybl.LocalDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.frybl.Model.Recipe;
import java.util.List;


@Dao
public interface RecipeDAO {

    @Insert
    long insert(Recipe recipe);

    @Transaction
    @Query("SELECT * FROM recipe_table")
    LiveData<List<Recipe>> getRecipes();


}
