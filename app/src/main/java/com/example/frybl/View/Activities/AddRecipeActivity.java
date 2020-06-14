package com.example.frybl.View.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.frybl.R;
import com.example.frybl.View.Fragments.AddRecipeDetailsFragment;
import com.example.frybl.View.Fragments.AddRecipeIngredientsFragment;
import com.example.frybl.View.Fragments.AddRecipeInstructionsFragment;
import com.example.frybl.ViewModel.AddRecipeViewModel;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.Locale;

import static com.example.frybl.View.Activities.MainActivity.component;

public class AddRecipeActivity extends AppCompatActivity implements AddRecipeInstructionsFragment.OnClickListenerInterface,AddRecipeIngredientsFragment.OnClickListenerInterface,AddRecipeDetailsFragment.OnClickListenerInterface {

    private AddRecipeDetailsFragment detailsFragment;
    private AddRecipeIngredientsFragment ingredientsFragment;
    private AddRecipeInstructionsFragment instructionsFragment;
    private MaterialButtonToggleGroup buttonToggleGroup;
    private AddRecipeViewModel viewModel;
    private ProgressBar progressBar;
    private int previouslyChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLanguage();
        setContentView(R.layout.activity_add_recipe);
        viewModel = new ViewModelProvider(this).get(AddRecipeViewModel.class);
        component.inject(viewModel);

        buttonToggleGroup = findViewById(R.id.toggleButton);
        progressBar = findViewById(R.id.upload_progress_bar);
        buttonToggleGroup.setSingleSelection(true);
        detailsFragment = new AddRecipeDetailsFragment();
        ingredientsFragment = new AddRecipeIngredientsFragment();
        instructionsFragment = new AddRecipeInstructionsFragment();
        progressBar.setVisibility(View.INVISIBLE);

        buttonToggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {

                if (isChecked) {
                    switch (checkedId) {
                        case R.id.details_fragment_button: {
                            openFragment(detailsFragment);
                            previouslyChecked = R.id.details_fragment_button;
                            break;
                        }
                        case R.id.ingredients_fragment_button: {
                            openFragment(ingredientsFragment);
                            previouslyChecked = R.id.ingredients_fragment_button;
                            break;
                        }
                        case R.id.instructions_fragment_button: {
                            openFragment(instructionsFragment);
                            previouslyChecked = R.id.instructions_fragment_button;
                            break;
                        }

                    }
                } else {
                    group.check(previouslyChecked);
                }
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, detailsFragment).commit();

        viewModel.getUploadIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading)
                {
                    progressBar.setVisibility(View.VISIBLE);
                }
                else
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    finish();
                }
            }
        });

        viewModel.getIsError().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isError) {
                if (isError)
                {
                    new AlertDialog.Builder(AddRecipeActivity.this).setTitle(getString(R.string.error_add_recipe)).setMessage(getString(R.string.error_add_recipe_text)).setPositiveButton(R.string.ok,null).show();
                }
            }
        });
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


    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
        transaction.replace(R.id.fragment_container,fragment);
        transaction.commit();
    }


    @Override
    public void onAddRecipeClicked() {
        viewModel.addRecipe(generateFileExtension(viewModel.getRecipeUri().getValue()));
    }

    @Override
    public void onNextToInstructionsClicked() {
        openFragment(instructionsFragment);
        previouslyChecked = R.id.instructions_fragment_button;
        buttonToggleGroup.check(previouslyChecked);
    }

    @Override
    public void onNextToIngredientsClicked() {
        openFragment(ingredientsFragment);
        previouslyChecked = R.id.ingredients_fragment_button;
        buttonToggleGroup.check(previouslyChecked);
    }

    private String generateFileExtension(Uri uri)
    {
        if (uri == null)
        {
            return null;
        }
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(viewModel.getRecipeUri().getValue()));
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(AddRecipeActivity.this).setTitle(getString(R.string.exit_add_recipe)).setMessage(getString(R.string.exit_add_recipe_message)).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNegativeButton(R.string.no,null).show();

    }
}
