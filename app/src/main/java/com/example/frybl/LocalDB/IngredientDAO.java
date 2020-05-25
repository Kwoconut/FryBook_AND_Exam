package com.example.frybl.LocalDB;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.frybl.Model.Ingredient;

import java.util.List;

@Dao
public interface IngredientDAO {

    @Insert
    void insert(Ingredient ingredient);

    @Insert
    void insertIngredients(List<Ingredient> ingredients);
}
