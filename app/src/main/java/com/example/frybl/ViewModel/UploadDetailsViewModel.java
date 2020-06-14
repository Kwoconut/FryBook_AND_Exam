package com.example.frybl.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.frybl.Model.Ingredient;
import com.example.frybl.Model.Instruction;
import com.example.frybl.Model.Upload;
import com.example.frybl.Repository.AppRepository;

import java.util.List;

import javax.inject.Inject;

public class UploadDetailsViewModel extends AndroidViewModel {

    @Inject
    public AppRepository repository;
    private MutableLiveData<String> selectedRecipeId;

    public UploadDetailsViewModel(@NonNull Application application) {
        super(application);
        selectedRecipeId = new MutableLiveData<String>();
    }

    public void setSelectedRecipeId(String recipeId)
    {
        selectedRecipeId.setValue(recipeId);
    }

    public LiveData<Upload> getUploadLiveData()
    {
        return repository.getUploadLiveData(selectedRecipeId.getValue());
    }

    public void rateRecipe(float rating) {
        repository.rateRecipe(rating,selectedRecipeId.getValue());
    }

    public void saveRecipe()
    {
        repository.saveRecipeToLocalDatabase();
    }

    public LiveData<List<Ingredient>> getIngredientLiveData()
    {
        return repository.getFirebaseIngredientLiveData(selectedRecipeId.getValue());
    }

    public LiveData<List<Instruction>> getInstructionLiveData()
    {
        return repository.getFirebaseInstructionLiveData(selectedRecipeId.getValue());

    }
}
