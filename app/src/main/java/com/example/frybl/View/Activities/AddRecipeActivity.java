package com.example.frybl.View.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;

import com.example.frybl.Model.Recipe;
import com.example.frybl.View.Fragments.AddRecipeDetailsFragment;
import com.example.frybl.View.Fragments.AddRecipeIngredientsFragment;
import com.example.frybl.View.Fragments.AddRecipeInstructionsFragment;
import com.example.frybl.R;
import com.example.frybl.ViewModel.AddRecipeViewModel;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.button.MaterialButtonToggleGroup;

public class AddRecipeActivity extends AppCompatActivity implements AddRecipeInstructionsFragment.OnClickListenerInterface {

    private AddRecipeDetailsFragment detailsFragment;
    private AddRecipeIngredientsFragment ingredientsFragment;
    private AddRecipeInstructionsFragment instructionsFragment;
    private MaterialButtonToggleGroup buttonToggleGroup;
    private AddRecipeViewModel viewModel;
    private int previouslyChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        viewModel = new ViewModelProvider(this).get(AddRecipeViewModel.class);
        buttonToggleGroup = findViewById(R.id.toggleButton);
        buttonToggleGroup.setSingleSelection(true);
        detailsFragment = new AddRecipeDetailsFragment();
        ingredientsFragment = new AddRecipeIngredientsFragment();
        instructionsFragment = new AddRecipeInstructionsFragment();

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
        finish();
    }

    private String generateFileExtension(Uri uri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(viewModel.getRecipeUri().getValue()));
    }

}
