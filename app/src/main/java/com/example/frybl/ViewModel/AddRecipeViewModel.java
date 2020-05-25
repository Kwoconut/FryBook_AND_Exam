package com.example.frybl.ViewModel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.frybl.DaggerComponents.AppModule;
import com.example.frybl.DaggerComponents.DaggerBackendComponent;
import com.example.frybl.DaggerComponents.BackendModule;
import com.example.frybl.Model.Ingredient;
import com.example.frybl.Model.Instruction;
import com.example.frybl.Model.Recipe;
import com.example.frybl.Repository.AppRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class AddRecipeViewModel extends AndroidViewModel {

    @Inject
    public AppRepository repository;

    private MutableLiveData<String> recipeName;
    private MutableLiveData<String> recipeDescription;
    private MutableLiveData<String> recipeCategory;
    private MutableLiveData<Integer> recipePrepTime;
    private MutableLiveData<Integer> recipeCookTime;
    private MutableLiveData<List<Ingredient>> recipeIngredients;
    private MutableLiveData<List<Instruction>> recipeInstructions;
    private MutableLiveData<Uri> recipeImageUri;

    public AddRecipeViewModel(@NonNull Application application) {
        super(application);
        DaggerBackendComponent.builder().appModule(new AppModule(application)).backendModule(new BackendModule(application)).build().inject(this);
        recipeName = new MutableLiveData<>();
        recipeDescription = new MutableLiveData<>();
        recipeCategory = new MutableLiveData<>();
        recipePrepTime = new MutableLiveData<>();
        recipeCookTime = new MutableLiveData<>();
        List<Ingredient> ingredients = new ArrayList<Ingredient>();
        recipeIngredients = new MutableLiveData<>();
        recipeIngredients.setValue(ingredients);
        List<Instruction> instructions = new ArrayList<Instruction>();
        recipeInstructions = new MutableLiveData<>();
        recipeInstructions.setValue(instructions);
        recipeImageUri = new MutableLiveData<>();
    }


    public void setRecipeName(String recipeName) {
        this.recipeName.setValue(recipeName);
    }

    public void setRecipeDescription(String recipeDescription) {
        this.recipeDescription.setValue(recipeDescription);
    }

    public void setRecipeCategory(String recipeCategory) {
        this.recipeCategory.setValue(recipeCategory);
    }

    public void setRecipePrepTime(int recipePrepTime) {
        this.recipePrepTime.setValue(recipePrepTime);
    }

    public void setRecipeCookTime(int recipeCookTime) {
        this.recipeCookTime.setValue(recipeCookTime);
    }

    public void setRecipeImageUri(Uri recipeImageUri) {
        this.recipeImageUri.setValue(recipeImageUri);
    }

    public LiveData<String> getRecipeName()
    {
        return recipeName;
    }

    public LiveData<String> getRecipeDescription()
    {
        return recipeDescription;
    }

    public LiveData<String> getRecipeCategory()
    {
        return recipeCategory;
    }

    public LiveData<Integer> getRecipePrepTime()
    {
        return recipePrepTime;
    }

    public LiveData<Integer> getRecipeCookTime()
    {
        return recipeCookTime;
    }

    public LiveData<Uri> getRecipeUri()
    {
        return recipeImageUri;
    }

    public LiveData<List<Ingredient>> getRecipeIngredients()
    {
        return recipeIngredients;
    }

    public LiveData<List<Instruction>> getRecipeInstructions()
    {
        return recipeInstructions;
    }

    public void addRecipeIngredient(Ingredient ingredient)
    {
        List<Ingredient> ingredients = recipeIngredients.getValue();
        ingredients.add(ingredient);
        recipeIngredients.setValue(ingredients);
    }

    public void addRecipeInstruction(Instruction instruction)
    {
        List<Instruction> instructions = recipeInstructions.getValue();
        instructions.add(instruction);
        recipeInstructions.setValue(instructions);
    }

    public void removeRecipeInstruction(Instruction instruction)
    {
        List<Instruction> instructions = recipeInstructions.getValue();
        instructions.remove(instruction);
        recipeInstructions.setValue(instructions);
    }

    public void removeRecipeIngredient(Ingredient ingredient)
    {
        List<Ingredient> ingredients = recipeIngredients.getValue();
        ingredients.remove(ingredient);
        recipeIngredients.setValue(ingredients);
    }

    public void addRecipe(String extension)
    {
        Recipe recipe = new Recipe(recipeName.getValue(),recipeDescription.getValue(),recipeCategory.getValue(),recipePrepTime.getValue(),recipeCookTime.getValue());
        recipe.setIngredients(recipeIngredients.getValue());
        recipe.setInstructions(recipeInstructions.getValue());
        repository.uploadNewRecipe(recipe, recipeImageUri.getValue(), extension);
    }


}
