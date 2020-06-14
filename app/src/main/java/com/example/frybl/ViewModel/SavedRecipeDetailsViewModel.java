package com.example.frybl.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.frybl.Model.Ingredient;
import com.example.frybl.Model.Instruction;
import com.example.frybl.Model.Recipe;
import com.example.frybl.Repository.AppRepository;

import java.util.List;

import javax.inject.Inject;

public class SavedRecipeDetailsViewModel extends AndroidViewModel {
    @Inject
    AppRepository appRepository;

    private MutableLiveData<String> selectedSavedRecipe;

    public SavedRecipeDetailsViewModel(@NonNull Application application) {
        super(application);
        this.selectedSavedRecipe = new MutableLiveData<>();
    }

    public MutableLiveData<String> getSelectedSavedRecipe() {
        return selectedSavedRecipe;
    }

    public void setSelectedSavedRecipe(String id) {
        this.selectedSavedRecipe.setValue(id);
    }

    public LiveData<Recipe> getRecipeLiveData() {
        return appRepository.getSavedRecipe(selectedSavedRecipe.getValue());
    }

    public LiveData<List<Instruction>> getInstructionLiveData() {
        return appRepository.getSavedInstructions(selectedSavedRecipe.getValue());
    }

    public LiveData<List<Ingredient>> getIngredientLiveData() {
        return appRepository.getSavedIngredients(selectedSavedRecipe.getValue());
    }
}
