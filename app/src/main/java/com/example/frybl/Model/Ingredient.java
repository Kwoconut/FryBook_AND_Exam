package com.example.frybl.Model;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity (tableName = "ingredient_table")
public class Ingredient {

    @PrimaryKey(autoGenerate = true)
    private int ingredientId;
    private int recipeId;
    private String name;
    private String quantity;
    private boolean ticked;

    @Ignore
    public Ingredient()
    {

    }

    public Ingredient(String name, String quantity, boolean ticked) {
        this.name = name;
        this.quantity = quantity;
        this.ticked = ticked;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public boolean isTicked() {
        return ticked;
    }

    public void setTicked(boolean ticked) {
        this.ticked = ticked;
    }

}
