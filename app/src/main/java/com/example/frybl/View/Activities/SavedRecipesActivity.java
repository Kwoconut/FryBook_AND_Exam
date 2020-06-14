package com.example.frybl.View.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frybl.Model.RecipeMinimal;
import com.example.frybl.R;
import com.example.frybl.View.Adapters.SavedRecipesAdapter;
import com.example.frybl.ViewModel.SavedRecipesViewModel;

import java.util.List;
import java.util.Locale;

import static com.example.frybl.View.Activities.MainActivity.component;

public class SavedRecipesActivity extends AppCompatActivity implements SavedRecipesAdapter.OnItemClickListener {

    SavedRecipesViewModel savedRecipesViewModel;
    RecyclerView savedRecipesRecyclerView;
    Button deleteAllRecipesButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLanguage();
        setContentView(R.layout.activity_saved_recipes);

        deleteAllRecipesButton = findViewById(R.id.saved_recipes_delete_button);

        deleteAllRecipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedRecipesViewModel.deleteAllSavedRecipes();
            }
        });

        savedRecipesRecyclerView = findViewById(R.id.saved_recipes_recycler_view);
        savedRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        savedRecipesRecyclerView.setHasFixedSize(true);
        final SavedRecipesAdapter adapter = new SavedRecipesAdapter();
        adapter.setListener(this);
        savedRecipesRecyclerView.setAdapter(adapter);

        savedRecipesViewModel = new ViewModelProvider(this).get(SavedRecipesViewModel.class);
        component.inject(savedRecipesViewModel);
        savedRecipesViewModel.getSavedRecipes().observe(this, new Observer<List<RecipeMinimal>>() {
            @Override
            public void onChanged(List<RecipeMinimal> recipes) {
                adapter.submitList(recipes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                new AlertDialog.Builder(SavedRecipesActivity.this).setTitle(getString(R.string.confirm_delete)).setMessage(getString(R.string.confirm_delete_message) + " " + adapter.getRecipeAt(viewHolder.getAdapterPosition()).getName() + "?").setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        savedRecipesViewModel.deleteRecipe(adapter.getRecipeAt(viewHolder.getAdapterPosition()).getRecipeId());
                    }
                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.notifyDataSetChanged();
                    }
                }).create().show();
            }
        }).attachToRecyclerView(savedRecipesRecyclerView);

    }

    @Override
    public void onItemClick(String recipeId) {
        Intent intent = new Intent(getBaseContext(), SavedRecipeDetailsActivity.class);
        intent.putExtra("DOCUMENT_ID",recipeId);
        startActivity(intent);
    }

    private void setLanguage(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("languageSettings", MODE_PRIVATE).edit();
        editor.putString("Language",language);
        editor.apply();
    }

    public void loadLanguage()
    {
        SharedPreferences preferences = getSharedPreferences("languageSettings", Activity.MODE_PRIVATE);
        String language = preferences.getString("Language","");
        setLanguage(language);
    }
}
