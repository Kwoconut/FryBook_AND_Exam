package com.example.frybl.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.frybl.Model.RecipeMinimal;
import com.example.frybl.Repository.AppRepository;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import javax.inject.Inject;

public class UserUploadsViewModel extends AndroidViewModel {

    @Inject
    AppRepository repository;

    public UserUploadsViewModel(@NonNull Application application) {
        super(application);
    }

    public FirestoreRecyclerOptions<RecipeMinimal> getFirestoreUserRecipeOptions()
    {
        return repository.getFirestoreUserRecipeOptions();
    }

    public void removeUpload(String recipeId) {
        repository.removeUpload(recipeId);
    }
}
