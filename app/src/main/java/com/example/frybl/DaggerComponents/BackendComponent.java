package com.example.frybl.DaggerComponents;

import android.app.Application;

import com.example.frybl.LocalDB.AppDatabase;
import com.example.frybl.Repository.AppRepository;
import com.example.frybl.ViewModel.AddRecipeViewModel;
import com.example.frybl.ViewModel.RecipeFeedViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(dependencies = {},modules = {AppModule.class, BackendModule.class})
public interface BackendComponent {
    void inject(AddRecipeViewModel addRecipeViewModel);

    void inject(RecipeFeedViewModel recipeFeedViewModel);

    AppDatabase appDatabase();

    AppRepository appRepository();

    Application application();
}
