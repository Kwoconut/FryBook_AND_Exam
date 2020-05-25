package com.example.frybl.Model;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
public class Upload {

    private String author;
    private Recipe recipe;
    private float rating;

    public Upload()
    {

    }

    public Upload(String author, Recipe recipe, float rating) {
        this.author = author;
        this.recipe = recipe;
        this.rating = rating;
    }

    public float getRating() {
        return rating;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Upload))
        {
            return false;
        }
        Upload other = (Upload) obj;
        return other.getRecipe().equals(this.getRecipe()) && other.getAuthor().equals(this.getAuthor());
    }
}
