package com.example.frybl.Repository;

import android.net.Uri;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.frybl.LocalDB.DAO.IngredientDAO;
import com.example.frybl.LocalDB.DAO.InstructionDAO;
import com.example.frybl.LocalDB.DAO.RecipeDAO;
import com.example.frybl.Model.Ingredient;
import com.example.frybl.Model.Instruction;
import com.example.frybl.Model.Quote;
import com.example.frybl.Model.Recipe;
import com.example.frybl.Model.RecipeMinimal;
import com.example.frybl.Model.Upload;
import com.example.frybl.Networking.Firebase.FirebaseManager;
import com.example.frybl.Networking.Retrofit.ApiManager;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppRepository {

    private InstructionDAO instructionDAO;
    private IngredientDAO ingredientDAO;
    private RecipeDAO recipeDAO;
    private FirebaseManager firebaseManager;
    private ApiManager apiManager;
    private LiveData<List<RecipeMinimal>> savedRecipesNames;
    private LiveData<List<Instruction>> firebaseSelectedUploadInstructions;
    private LiveData<List<Ingredient>> firebaseSelectedUploadIngredients;
    private LiveData<Upload> firebaseSelectedUpload;

    @Inject
    public AppRepository(FirebaseManager firebaseManager, InstructionDAO instructionDAO, IngredientDAO ingredientDAO, RecipeDAO recipeDAO, ApiManager apiManager)
    {
        this.firebaseManager = firebaseManager;
        this.instructionDAO = instructionDAO;
        this.ingredientDAO = ingredientDAO;
        this.recipeDAO = recipeDAO;
        this.apiManager = apiManager;
        this.savedRecipesNames = recipeDAO.getAllRecipeNames();
    }

    public void uploadNewRecipe(Recipe recipe, Uri uri, String extension) {
        firebaseManager.uploadNewRecipe(recipe,uri,extension);
    }

    public void removeUpload(String recipeId) {
        firebaseManager.removeUpload(recipeId);
    }

    public FirestoreRecyclerOptions<Upload> getFirestoreFeedOptions()
    {
        return firebaseManager.getFirestoreFeedOptions();
    }

    public FirestoreRecyclerOptions<RecipeMinimal> getFirestoreUserRecipeOptions()
    {
        return firebaseManager.getFirestoreUserRecipeOptions();
    }

    public LiveData<Upload> getUploadLiveData(String recipeId)
    {
        firebaseSelectedUpload = firebaseManager.getUploadLiveData(recipeId);
        return firebaseSelectedUpload;
    }

    public LiveData<List<Ingredient>> getFirebaseIngredientLiveData(String value) {
        firebaseSelectedUploadIngredients = firebaseManager.getIngredientListLiveData(value);
        return firebaseSelectedUploadIngredients;
    }

    public LiveData<List<Instruction>> getFirebaseInstructionLiveData(String value) {
        firebaseSelectedUploadInstructions = firebaseManager.getInstructionListLiveData(value);
        return firebaseSelectedUploadInstructions;
    }

    public LiveData<Boolean> getUploadIsLoading()
    {
        return firebaseManager.getUploadIsLoading();
    }

    public LiveData<Boolean> isErrorLogin()
    {
        return firebaseManager.isErrorLogin();
    }

    public void loginUser(String email, String password) {
        firebaseManager.loginUser(email,password);
    }

    public void registerUser(String name, String email, String password) {
        firebaseManager.registerUser(name,email,password);
    }

    public LiveData<FirebaseUser> getCurrentUser() {
        return firebaseManager.getCurrentUser();
    }

    public void logout() {
        firebaseManager.logout();
    }

    public void rateRecipe(float rating, String recipeId) {
        firebaseManager.rateRecipe(rating, recipeId);
    }

    public LiveData<Quote> getQuote()
    {
        return apiManager.getQuoteLiveData();
    }

    public void requestQuote() {
        apiManager.requestQuote();
    }

    public void saveRecipeToLocalDatabase() {
        new InsertRecipeAsync(recipeDAO).execute(firebaseSelectedUpload.getValue().getRecipe());
        for (int i = 0 ; i < firebaseSelectedUploadIngredients.getValue().size();i++)
        {
            new InsertIngredientAsync(ingredientDAO).execute(firebaseSelectedUploadIngredients.getValue().get(i));
        }
        for (int i = 0 ; i < firebaseSelectedUploadInstructions.getValue().size();i++)
        {
            new InsertInstructionAsync(instructionDAO).execute(firebaseSelectedUploadInstructions.getValue().get(i));
        }
    }

    public void deleteAllSavedRecipes() {
        new DeleteAllSavedRecipesAsync(recipeDAO,instructionDAO,ingredientDAO).execute();
    }

    public void deleteRecipe(String id) {
        new DeleteRecipeByIdAsync(this.recipeDAO,this.instructionDAO,this.ingredientDAO).execute(id);
    }

    public LiveData<List<RecipeMinimal>> getSavedRecipesNames() {
        return savedRecipesNames;
    }

    public LiveData<Recipe> getSavedRecipe(String id) {
        return recipeDAO.getRecipeById(id);
    }

    public LiveData<List<Instruction>> getSavedInstructions(String value) {
        return instructionDAO.getAllInstructionsByRecipeId(value);
    }

    public LiveData<List<Ingredient>> getSavedIngredients(String value) {
        return ingredientDAO.getAllIngredientsByRecipeId(value);
    }


    private static class InsertRecipeAsync extends AsyncTask<Recipe,Void,Void>
    {
        private RecipeDAO recipeDAO;

        private InsertRecipeAsync(RecipeDAO recipeDAO)
        {
            this.recipeDAO = recipeDAO;
        }

        @Override
        protected Void doInBackground(Recipe... recipes) {
            recipeDAO.insert(recipes[0]);
            return null;
        }
    }

    private static class InsertIngredientAsync extends AsyncTask<Ingredient,Void,Void>
    {
        private IngredientDAO ingredientDAO;

        private InsertIngredientAsync(IngredientDAO ingredientDAO)
        {
            this.ingredientDAO = ingredientDAO;
        }

        @Override
        protected Void doInBackground(Ingredient... ingredients) {
            ingredientDAO.insert(ingredients[0]);
            return null;
        }
    }

    private static class InsertInstructionAsync extends AsyncTask<Instruction,Void,Void>
    {
        private InstructionDAO instructionDAO;

        private InsertInstructionAsync(InstructionDAO instructionDAO)
        {
            this.instructionDAO = instructionDAO;
        }

        @Override
        protected Void doInBackground(Instruction... instructions) {
            instructionDAO.insert(instructions[0]);
            return null;
        }
    }

    private static class DeleteAllSavedRecipesAsync extends AsyncTask<Void,Void,Void>
    {
        private RecipeDAO recipeDAO;
        private InstructionDAO instructionDAO;
        private IngredientDAO ingredientDAO;

        private DeleteAllSavedRecipesAsync(RecipeDAO recipeDAO,InstructionDAO instructionDAO, IngredientDAO ingredientDAO)
        {
            this.recipeDAO = recipeDAO;
            this.instructionDAO = instructionDAO;
            this.ingredientDAO = ingredientDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            recipeDAO.deleteAllRecipes();
            instructionDAO.deleteAllInstructions();
            ingredientDAO.deleteAllIngredients();
            return null;
        }
    }

    private static class DeleteRecipeByIdAsync extends AsyncTask<String,Void,Void>
    {
        private RecipeDAO recipeDAO;
        private InstructionDAO instructionDAO;
        private IngredientDAO ingredientDAO;

        private DeleteRecipeByIdAsync(RecipeDAO recipeDAO, InstructionDAO instructionDAO, IngredientDAO ingredientDAO)
        {
            this.recipeDAO = recipeDAO;
            this.instructionDAO = instructionDAO;
            this.ingredientDAO = ingredientDAO;
        }

        @Override
        protected Void doInBackground(String... strings) {
            ingredientDAO.deleteIngredientsByRecipeId(strings[0]);
            instructionDAO.deleteInstructionsByRecipeId(strings[0]);
            recipeDAO.deleteRecipeById(strings[0]);
            return null;
        }
    }
}
