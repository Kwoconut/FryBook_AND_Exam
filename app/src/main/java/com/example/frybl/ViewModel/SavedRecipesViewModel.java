package com.example.frybl.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.frybl.Model.RecipeMinimal;
import com.example.frybl.Repository.AppRepository;

import java.util.List;

import javax.inject.Inject;

public class SavedRecipesViewModel extends AndroidViewModel {

    @Inject
    AppRepository appRepository;

    public SavedRecipesViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<RecipeMinimal>> getSavedRecipes()
    {
        return appRepository.getSavedRecipesNames();
    }

    public void deleteRecipe(String id)
    {
        appRepository.deleteRecipe(id);
    }

    public void deleteAllSavedRecipes() {
        appRepository.deleteAllSavedRecipes();
    }
}
