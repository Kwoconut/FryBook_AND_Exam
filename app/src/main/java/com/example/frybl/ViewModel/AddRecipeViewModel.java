package com.example.frybl.ViewModel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.frybl.Model.Ingredient;
import com.example.frybl.Model.Instruction;
import com.example.frybl.Model.Recipe;
import com.example.frybl.R;
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
    private MutableLiveData<String> recipeNameError;
    private MutableLiveData<String> recipeCategoryError;
    private MutableLiveData<String> recipePrepTimeError;
    private MutableLiveData<String> recipeCookTimeError;
    private MutableLiveData<Boolean> isError;

    public AddRecipeViewModel(@NonNull Application application) {
        super(application);
        recipeName = new MutableLiveData<>();
        recipeDescription = new MutableLiveData<>();
        recipeCategory = new MutableLiveData<>();
        recipePrepTime = new MutableLiveData<>();
        recipeCookTime = new MutableLiveData<>();
        recipeIngredients = new MutableLiveData<>();
        recipeInstructions = new MutableLiveData<>();
        recipeImageUri = new MutableLiveData<>();
        recipeNameError = new MutableLiveData<>();
        recipeCategoryError = new MutableLiveData<>();
        recipeCookTimeError = new MutableLiveData<>();
        recipePrepTimeError = new MutableLiveData<>();
        isError = new MutableLiveData<>();
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

    public LiveData<String> getRecipeNameError() {
        return recipeNameError;
    }

    public LiveData<String> getRecipeCategoryError() {
        return recipeCategoryError;
    }

    public LiveData<String> getRecipePrepTimeError() {
        return recipePrepTimeError;
    }

    public LiveData<String> getRecipeCookTimeError() {
        return recipeCookTimeError;
    }

    public LiveData<Boolean> getIsError() {
        return isError;
    }

    public LiveData<Boolean> getUploadIsLoading()
    {
        return repository.getUploadIsLoading();
    }

    public void addRecipeIngredient(Ingredient ingredient)
    {
        List<Ingredient> ingredients = recipeIngredients.getValue();
        if (ingredients == null)
        {
            ingredients = new ArrayList<>();
        }
        ingredients.add(ingredient);
        recipeIngredients.setValue(ingredients);
    }

    public void addRecipeInstruction(Instruction instruction)
    {
        List<Instruction> instructions = recipeInstructions.getValue();
        if (instructions == null)
        {
            instructions = new ArrayList<>();
        }
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

        checkFields();
        if (!isError.getValue()) {
            Recipe recipe = new Recipe(recipeName.getValue(), recipeDescription.getValue(), recipeCategory.getValue(), recipePrepTime.getValue(), recipeCookTime.getValue());
            recipe.setIngredients(recipeIngredients.getValue());
            recipe.setInstructions(recipeInstructions.getValue());
            repository.uploadNewRecipe(recipe, recipeImageUri.getValue(), extension);
        }

        isError.setValue(false);
    }

    public void checkFields()
    {
        isError.setValue(false);
        if (!isValidName() && !isError.getValue())
        {
            isError.setValue(true);
        }
        if (!isValidPrepTime() && !isError.getValue())
        {
            isError.setValue(true);
        }
        if (!isValidCookTime() && !isError.getValue())
        {
            isError.setValue(true);
        }
        if ((getRecipeIngredients().getValue() == null || getRecipeIngredients().getValue().isEmpty() || getRecipeInstructions().getValue() == null || getRecipeInstructions().getValue().isEmpty() || recipeImageUri.getValue() == null) && !isError.getValue())
        {
            isError.setValue(true);
        }
    }

    public boolean isValidName()
    {
        if (recipeName.getValue() == null ||recipeName.getValue().isEmpty())
        {
            recipeNameError.setValue(getApplication().getString(R.string.field_required));
            return false;
        }
        else if (recipeName.getValue().length() <= 3) {
            recipeNameError.setValue(getApplication().getString(R.string.name_less_than_4));
            return false;
        }
        else
        {
            recipeNameError.setValue("");
            return true;
        }
    }


    public boolean isValidPrepTime()
    {
        if (recipePrepTime.getValue() == null || recipePrepTime.getValue() == 0)
        {
            recipePrepTimeError.setValue(getApplication().getString(R.string.field_required));
            return false;
        }
        else if (recipePrepTime.getValue() >= 1000)
        {
            recipePrepTimeError.setValue(getApplication().getString(R.string.prepTime_error));
            return false;
        }
        else
        {
            recipePrepTimeError.setValue("");
            return true;
        }
    }

    public boolean isValidCookTime()
    {
        if (recipeCookTime.getValue() == null || recipeCookTime.getValue() == 0)
        {
            recipeCookTimeError.setValue(getApplication().getString(R.string.field_required));
            return false;
        }
        else if (recipeCookTime.getValue() >= 1000)
        {
            recipeCookTimeError.setValue(getApplication().getString(R.string.prepTime_error));
            return false;
        }
        else
        {
            recipeCookTimeError.setValue("");
            return true;
        }
    }


}
