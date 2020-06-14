package com.example.frybl.Model;

public class RecipeMinimal {
    private String recipeId;
    private String name;

    public RecipeMinimal(String recipeId, String name) {
        this.recipeId = recipeId;
        this.name = name;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
