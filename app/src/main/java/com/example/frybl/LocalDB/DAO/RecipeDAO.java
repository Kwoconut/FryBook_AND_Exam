package com.example.frybl.LocalDB.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.frybl.Model.Recipe;
import com.example.frybl.Model.RecipeMinimal;

import java.util.List;


@Dao
public interface RecipeDAO {

    @Insert
    long insert(Recipe recipe);

    @Query("SELECT * FROM recipe_table")
    LiveData<List<Recipe>> getAllRecipes();

    @Query("SELECT * FROM recipe_table WHERE recipeId = :id")
    LiveData<Recipe> getRecipeById(String id);

    @Query("SELECT recipeId,name FROM recipe_table")
    LiveData<List<RecipeMinimal>> getAllRecipeNames();

    @Query("DELETE FROM recipe_table WHERE recipeId = :id")
    void deleteRecipeById(String id);

    @Query("DELETE FROM recipe_table")
    void deleteAllRecipes();
}
