package com.example.frybl.LocalDB.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.frybl.Model.Ingredient;

import java.util.List;

@Dao
public interface IngredientDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ingredient ingredient);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIngredients(List<Ingredient> ingredients);

    @Query("SELECT * FROM ingredient_table WHERE recipeId = :id")
    LiveData<List<Ingredient>> getAllIngredientsByRecipeId(String id);

    @Query("DELETE FROM ingredient_table WHERE recipeId = :id")
    void deleteIngredientsByRecipeId(String id);

    @Query("DELETE FROM ingredient_table")
    void deleteAllIngredients();
}
