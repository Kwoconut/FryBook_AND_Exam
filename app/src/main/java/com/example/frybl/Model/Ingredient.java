package com.example.frybl.Model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity (tableName = "ingredient_table")
public class Ingredient {

    @Ignore
    public static final String FIRESTOREKEY_INGREDIENT_NAME = "name";
    @Ignore
    public static final String FIRESTOREKEY_INGREDIENT_QUANTITY = "quantity";
    @Ignore
    public static final String FIRESTOREKEY_INGREDIENT_PARENTRECIPE = "recipeId";

    @PrimaryKey(autoGenerate = true)
    private int ingredientId;
    private String name;
    private String quantity;
    private boolean ticked;
    private String recipeId;

    @Ignore
    public Ingredient()
    {

    }

    public Ingredient(String name, String quantity, boolean ticked) {
        this.name = name;
        this.quantity = quantity;
        this.ticked = ticked;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
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
