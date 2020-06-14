package com.example.frybl.Utility.DaggerComponents;

import android.app.Application;

import com.example.frybl.LocalDB.AppDatabase;
import com.example.frybl.LocalDB.DAO.IngredientDAO;
import com.example.frybl.LocalDB.DAO.InstructionDAO;
import com.example.frybl.LocalDB.DAO.RecipeDAO;
import com.example.frybl.Networking.Firebase.FirebaseManager;
import com.example.frybl.Networking.Retrofit.ApiManager;
import com.example.frybl.Repository.AppRepository;
import com.example.frybl.ViewModel.AddRecipeViewModel;
import com.example.frybl.ViewModel.LoginViewModel;
import com.example.frybl.ViewModel.RecipeFeedViewModel;
import com.example.frybl.ViewModel.SavedRecipeDetailsViewModel;
import com.example.frybl.ViewModel.SavedRecipesViewModel;
import com.example.frybl.ViewModel.SignUpViewModel;
import com.example.frybl.ViewModel.UploadDetailsViewModel;
import com.example.frybl.ViewModel.UserUploadsViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(dependencies = {},modules = {AppModule.class, LocalDBModule.class, FirebaseModule.class, RetrofitModule.class})
public interface RepositoryComponent {
    void inject(AddRecipeViewModel addRecipeViewModel);

    void inject(RecipeFeedViewModel recipeFeedViewModel);

    void inject(UploadDetailsViewModel uploadDetailsViewModel);

    void inject(SavedRecipesViewModel savedRecipesViewModel);

    void inject(SavedRecipeDetailsViewModel savedRecipeDetailsViewModel);

    void inject(LoginViewModel loginViewModel);

    void inject(SignUpViewModel signUpViewModel);

    void inject(UserUploadsViewModel userUploadsViewModel);

    IngredientDAO ingredientDAO();

    InstructionDAO instructionDAO();

    RecipeDAO recipeDAO();

    AppDatabase appDatabase();

    AppRepository appRepository();

    Application application();

    FirebaseManager firebaseManager();

    ApiManager apiManager();
}
