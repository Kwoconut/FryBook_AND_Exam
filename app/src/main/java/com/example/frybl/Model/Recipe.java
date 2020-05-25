package com.example.frybl.Model;

import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.util.ArrayList;
import java.util.List;

@Entity (tableName = "recipe_table")
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    private int recipeId;
    private String name;
    private String description;
    private String category;
    private int preparationTime;
    private int cookTime;
    private String imageUri;
    @Ignore
    private List<Ingredient> ingredients;
    @Ignore
    private List<Instruction> instructions;

    @Ignore
    public Recipe()
    {

    }

    public Recipe(String name, String description, String category, int preparationTime, int cookTime) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.preparationTime = preparationTime;
        this.cookTime = cookTime;
        this.ingredients = new ArrayList<>();
        this.instructions = new ArrayList<>();
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }

    public int getCookTime() {
        return cookTime;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Recipe)) {
            return false;
        }
        Recipe other = (Recipe) obj;

        return this.getName().equals(other.getName()) && this.getDescription().equals(other.getDescription()) && this.getCategory().equals(other.getCategory()) && this.getPreparationTime() == other.getPreparationTime() && this.getCookTime() == other.getCookTime();

    }
}
