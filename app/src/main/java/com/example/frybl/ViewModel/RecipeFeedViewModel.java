package com.example.frybl.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.frybl.Model.Upload;
import com.example.frybl.Repository.AppRepository;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

public class RecipeFeedViewModel extends AndroidViewModel {

    @Inject
    public AppRepository repository;

    public RecipeFeedViewModel(@NonNull Application application) {
        super(application);
    }

    public FirestoreRecyclerOptions<Upload> getFirestoreFeedOptions()
    {
        return repository.getFirestoreFeedOptions();
    }

    public LiveData<FirebaseUser> getCurrentUser() {
        return repository.getCurrentUser();
    }

    public void logout() {
        repository.logout();
    }
}
