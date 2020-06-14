package com.example.frybl.View.Activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.frybl.Model.Recipe;
import com.example.frybl.R;
import com.example.frybl.View.Fragments.SavedRecipeDetailsIngredientsFragment;
import com.example.frybl.View.Fragments.SavedRecipeDetailsInstructionsFragment;
import com.example.frybl.ViewModel.SavedRecipeDetailsViewModel;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import static com.example.frybl.View.Activities.MainActivity.component;

public class SavedRecipeDetailsActivity extends AppCompatActivity {

    SavedRecipeDetailsViewModel viewModel;
    ImageView savedRecipeDetailsImage;
    TextView savedRecipeDetailsTitleTextView;
    TextView savedRecipeDetailsCategoryTextView;
    TextView savedRecipeDetailsDescriptionTextView;
    TextView savedRecipeDetailsCookTimeTextView;
    TextView savedRecipeDetailsPreparationTimeTextView;
    TextView savedRecipeDetailsTotalTimeTextView;
    SavedRecipeDetailsIngredientsFragment ingredientsFragment;
    SavedRecipeDetailsInstructionsFragment instructionsFragment;
    MaterialButtonToggleGroup buttonToggleGroup;
    int previouslyChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLanguage();
        setContentView(R.layout.activity_saved_recipe_details);
        savedRecipeDetailsImage = findViewById(R.id.saved_recipe_details_image);
        savedRecipeDetailsTitleTextView = findViewById(R.id.saved_recipe_details_title);
        savedRecipeDetailsCategoryTextView = findViewById(R.id.saved_recipe_details_category);
        savedRecipeDetailsDescriptionTextView = findViewById(R.id.saved_recipe_details_description);
        savedRecipeDetailsCookTimeTextView = findViewById(R.id.saved_recipe_details_cook_time);
        savedRecipeDetailsPreparationTimeTextView = findViewById(R.id.saved_recipe_details_preparation_time);
        savedRecipeDetailsTotalTimeTextView = findViewById(R.id.saved_recipe_details_total_time);
        ingredientsFragment = new SavedRecipeDetailsIngredientsFragment();
        instructionsFragment = new SavedRecipeDetailsInstructionsFragment();
        buttonToggleGroup = findViewById(R.id.saved_recipe_details_toggle_button);
        buttonToggleGroup.setSingleSelection(true);

        viewModel = new ViewModelProvider(this).get(SavedRecipeDetailsViewModel.class);
        component.inject(viewModel);
        String id = getIntent().getStringExtra("DOCUMENT_ID");
        viewModel.setSelectedSavedRecipe(id);
        viewModel.getRecipeLiveData().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                Picasso.get().load(recipe.getImageUri()).fit().centerCrop().into(savedRecipeDetailsImage);
                savedRecipeDetailsTitleTextView.setText(recipe.getName());
                savedRecipeDetailsCategoryTextView.setText(recipe.getCategory());
                savedRecipeDetailsDescriptionTextView.setText(recipe.getDescription());
                savedRecipeDetailsCookTimeTextView.setText(recipe.getCookTime() + "m");
                savedRecipeDetailsPreparationTimeTextView.setText(recipe.getPreparationTime() + "m");
                int total_time = recipe.getTotalTime();
                savedRecipeDetailsTotalTimeTextView.setText(total_time + "m");

            }
        });

        buttonToggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {

                if (isChecked) {
                    switch (checkedId) {
                        case R.id.saved_recipe_ingredients_fragment_button: {
                            openFragment(ingredientsFragment);
                            previouslyChecked = R.id.saved_recipe_ingredients_fragment_button;
                            break;
                        }
                        case R.id.saved_recipe_instructions_fragment_button: {
                            openFragment(instructionsFragment);
                            previouslyChecked = R.id.saved_recipe_instructions_fragment_button;
                            break;
                        }

                    }
                } else {
                    group.check(previouslyChecked);
                }
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.saved_recipe_details_fragment_container, ingredientsFragment).commit();


    }

    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
        transaction.replace(R.id.saved_recipe_details_fragment_container,fragment);
        transaction.commit();
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
