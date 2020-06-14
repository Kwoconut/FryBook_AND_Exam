package com.example.frybl.View.Activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.frybl.Model.Upload;
import com.example.frybl.R;
import com.example.frybl.View.Fragments.DialogRateRecipeFragment;
import com.example.frybl.View.Fragments.UploadDetailsIngredientsFragment;
import com.example.frybl.View.Fragments.UploadDetailsInstructionsFragment;
import com.example.frybl.ViewModel.UploadDetailsViewModel;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import static com.example.frybl.View.Activities.MainActivity.component;

public class UploadDetailsActivity extends AppCompatActivity {

    UploadDetailsViewModel viewModel;
    ImageView uploadDetailsImage;
    TextView uploadDetailsTitleTextView;
    TextView uploadDetailsCategoryTextView;
    TextView uploadDetailsAuthorTextView;
    TextView uploadDetailsDescriptionTextView;
    TextView uploadDetailsCookTimeTextView;
    TextView uploadDetailsPreparationTimeTextView;
    TextView uploadDetailsTotalTimeTextView;
    RatingBar uploadDetailsRatingBar;
    MaterialButtonToggleGroup buttonToggleGroup;
    UploadDetailsIngredientsFragment ingredientsFragment;
    UploadDetailsInstructionsFragment instructionsFragment;
    Button uploadDetailsSaveButton;
    Button uploadDetailsRateButton;
    int previouslyChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLanguage();
        setContentView(R.layout.activity_upload_details);
        uploadDetailsImage = findViewById(R.id.upload_details_image);
        uploadDetailsTitleTextView = findViewById(R.id.upload_details_title);
        uploadDetailsCategoryTextView = findViewById(R.id.upload_details_category);
        uploadDetailsAuthorTextView = findViewById(R.id.upload_details_author);
        uploadDetailsDescriptionTextView = findViewById(R.id.upload_details_description);
        uploadDetailsCookTimeTextView = findViewById(R.id.upload_details_cook_time);
        uploadDetailsPreparationTimeTextView = findViewById(R.id.upload_details_preparation_time);
        uploadDetailsTotalTimeTextView = findViewById(R.id.upload_details_total_time);
        uploadDetailsRatingBar = findViewById(R.id.upload_details_rating);
        buttonToggleGroup = findViewById(R.id.upload_details_toggle_button);
        uploadDetailsSaveButton = findViewById(R.id.upload_details_save_button);
        uploadDetailsRateButton = findViewById(R.id.upload_details_rate_button);
        buttonToggleGroup.setSingleSelection(true);


        viewModel = new ViewModelProvider(this).get(UploadDetailsViewModel.class);
        component.inject(viewModel);
        String id = getIntent().getStringExtra("DOCUMENT_ID");
        viewModel.setSelectedRecipeId(id);
        ingredientsFragment = new UploadDetailsIngredientsFragment();
        instructionsFragment = new UploadDetailsInstructionsFragment();
        uploadDetailsRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        uploadDetailsSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.saveRecipe();
            }
        });

        buttonToggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {

                if (isChecked) {
                    switch (checkedId) {
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

        getSupportFragmentManager().beginTransaction().replace(R.id.upload_details_fragment_container, instructionsFragment).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.upload_details_fragment_container, ingredientsFragment).commit();




        viewModel.getUploadLiveData().observe(this, new Observer<Upload>() {
            @Override
            public void onChanged(Upload upload) {
                if (upload != null)
                {
                    Picasso.get().load(upload.getRecipe().getImageUri()).fit().centerCrop().into(uploadDetailsImage);
                    uploadDetailsTitleTextView.setText(upload.getRecipe().getName());
                    uploadDetailsCategoryTextView.setText(upload.getRecipe().getCategory());
                    uploadDetailsAuthorTextView.setText("by " + upload.getAuthor());
                    uploadDetailsDescriptionTextView.setText(upload.getRecipe().getDescription());
                    uploadDetailsCookTimeTextView.setText(upload.getRecipe().getCookTime() + "m");
                    uploadDetailsPreparationTimeTextView.setText(upload.getRecipe().getPreparationTime() + "m");
                    int total_time = upload.getRecipe().getTotalTime();
                    uploadDetailsTotalTimeTextView.setText(total_time + "m");
                    uploadDetailsRatingBar.setRating(upload.getRating());
                }
            }
        });


    }

    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
        transaction.replace(R.id.upload_details_fragment_container,fragment);
        transaction.commit();
    }

    private void openDialog()
    {
        DialogRateRecipeFragment dialog = new DialogRateRecipeFragment();
        dialog.show(getSupportFragmentManager(),"Rate recipe");
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
