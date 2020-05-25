package com.example.frybl.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.frybl.DaggerComponents.AppModule;
import com.example.frybl.DaggerComponents.BackendModule;
import com.example.frybl.DaggerComponents.DaggerBackendComponent;
import com.example.frybl.Model.Upload;
import com.example.frybl.Repository.AppRepository;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import javax.inject.Inject;

public class RecipeFeedViewModel extends AndroidViewModel {

    @Inject
    public AppRepository repository;

    public RecipeFeedViewModel(@NonNull Application application) {
        super(application);
        DaggerBackendComponent.builder().appModule(new AppModule(application)).backendModule(new BackendModule(application)).build().inject(this);
    }

    public FirestoreRecyclerOptions<Upload> getFirestoreOptions()
    {
        return repository.getFirestoreOptions();
    }
}
