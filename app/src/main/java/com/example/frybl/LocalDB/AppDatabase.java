package com.example.frybl.LocalDB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.frybl.LocalDB.DAO.IngredientDAO;
import com.example.frybl.LocalDB.DAO.InstructionDAO;
import com.example.frybl.LocalDB.DAO.RecipeDAO;
import com.example.frybl.Model.Ingredient;
import com.example.frybl.Model.Instruction;
import com.example.frybl.Model.Recipe;

@Database(entities = {Recipe.class, Ingredient.class, Instruction.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract RecipeDAO recipeDAO();

    public abstract IngredientDAO ingredientDAO();

    public abstract InstructionDAO instructionDAO();
}
