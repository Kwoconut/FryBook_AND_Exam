package com.example.frybl.View.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.frybl.R;
import com.example.frybl.View.Activities.AddRecipeActivity;
import com.example.frybl.ViewModel.AddRecipeViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

public class AddRecipeDetailsFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button uploadButton;
    private Button nextButton;
    private ImageView imageView;
    private AddRecipeViewModel viewModel;
    private TextInputLayout recipeNameLayout;
    private TextInputLayout recipeCategorySelectorLayout;
    private TextInputLayout recipePrepTimeLayout;
    private TextInputLayout recipeCookTimeLayout;
    private TextInputEditText recipeNameTextField;
    private TextInputEditText recipeDescriptionTextField;
    private TextInputEditText recipePrepTimeTextField;
    private TextInputEditText recipeCookTimeTextField;
    private AutoCompleteTextView recipeCategorySelector;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_recipe_details,container,false);


        recipeNameLayout = v.findViewById(R.id.details_recipe_name);
        recipeCategorySelectorLayout = v.findViewById(R.id.details_selector_layout);
        recipePrepTimeLayout = v.findViewById(R.id.details_recipe_preparationTime);
        recipeCookTimeLayout = v.findViewById(R.id.details_recipe_cookTime);
        recipeNameTextField = v.findViewById(R.id.details_recipe_name_textField);
        recipeDescriptionTextField = v.findViewById(R.id.details_recipe_description_textField);
        recipePrepTimeTextField = v.findViewById(R.id.details_recipe_preparationTime_textField);
        recipeCookTimeTextField = v.findViewById(R.id.details_recipe_cookTime_textField);
        recipeCategorySelector = v.findViewById(R.id.details_selector);

        imageView = v.findViewById(R.id.add_recipe_image_preview);
        nextButton = v.findViewById(R.id.details_next_button);
        uploadButton = v.findViewById(R.id.recipe_upload_image_button);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AddRecipeActivity)getActivity()).onNextToIngredientsClicked();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICK_IMAGE_REQUEST);
            }
        });

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        viewModel.setRecipeName(recipeNameTextField.getText().toString());
        viewModel.setRecipeCategory(recipeCategorySelector.getText().toString());
        viewModel.setRecipeDescription(recipeDescriptionTextField.getText().toString());
        try {
            viewModel.setRecipePrepTime(Integer.parseInt(recipePrepTimeTextField.getText().toString()));
            viewModel.setRecipeCookTime(Integer.parseInt(recipeCookTimeTextField.getText().toString()));
        }
        catch (NumberFormatException e) {
            viewModel.setRecipePrepTime(0);
            viewModel.setRecipeCookTime(0);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(AddRecipeViewModel.class);
        String[] categories = new String[] {
                getString(R.string.recipe_category_1),getString(R.string.recipe_category_2),getString(R.string.recipe_category_3),
                getString(R.string.recipe_category_4),getString(R.string.recipe_category_5),getString(R.string.recipe_category_6),
                getString(R.string.recipe_category_7)};
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        getContext(),
                        R.layout.dropdown_menu_popup_item,
                        categories);

        recipeCategorySelector.setAdapter(adapter);


        viewModel.getRecipeName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                recipeNameTextField.setText(s);
            }
        });

        viewModel.getRecipeCategory().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                recipeCategorySelector.setText(s);
            }
        });

        viewModel.getRecipeDescription().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                recipeDescriptionTextField.setText(s);
            }
        });

        viewModel.getRecipeCookTime().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                recipeCookTimeTextField.setText(String.valueOf(integer));
            }
        });

        viewModel.getRecipePrepTime().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                recipePrepTimeTextField.setText(String.valueOf(integer));
            }
        });

        viewModel.getRecipeUri().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                imageView.setImageURI(uri);
            }
        });

        viewModel.getRecipeNameError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                recipeNameLayout.setError(s);
            }
        });

        viewModel.getRecipeCategoryError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                recipeCategorySelectorLayout.setError(s);
            }
        });

        viewModel.getRecipeCookTimeError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                recipeCookTimeLayout.setError(s);
            }
        });

        viewModel.getRecipePrepTimeError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                recipePrepTimeLayout.setError(s);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            Uri imageUri = data.getData();
            viewModel.setRecipeImageUri(imageUri);
            Picasso.get().load(imageUri).fit().centerCrop().into(imageView);
        }
    }

    public interface OnClickListenerInterface
    {
        void onNextToIngredientsClicked();
    }


}
