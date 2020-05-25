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
import com.example.frybl.ViewModel.AddRecipeViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

public class AddRecipeDetailsFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button uploadButton;
    private ImageView imageView;
    private AddRecipeViewModel viewModel;
    private TextInputEditText recipeNameTextField;
    private TextInputEditText recipeDescriptionTextField;
    private TextInputEditText recipePrepTimeTextField;
    private TextInputEditText recipeCookTimeTextField;
    private AutoCompleteTextView recipeCategorySelector;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_recipe_details,container,false);


        recipeNameTextField = v.findViewById(R.id.details_recipe_name_textField);
        recipeDescriptionTextField = v.findViewById(R.id.details_recipe_description_textField);
        recipePrepTimeTextField = v.findViewById(R.id.details_recipe_preparationTime_textField);
        recipeCookTimeTextField = v.findViewById(R.id.details_recipe_cookTime_textField);
        recipeCategorySelector = v.findViewById(R.id.details_selector);

        String[] COUNTRIES = new String[] {"Item 1", "Item 2", "Item 3", "Item 4"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        getContext(),
                        R.layout.dropdown_menu_popup_item,
                        COUNTRIES);

        recipeCategorySelector.setAdapter(adapter);

        imageView = v.findViewById(R.id.add_recipe_image_preview);
        uploadButton = v.findViewById(R.id.recipe_upload_image_button);

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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            Uri imageUri = data.getData();
            viewModel.setRecipeImageUri(imageUri);
            Picasso.get().load(imageUri).into(imageView);
        }
    }


}
