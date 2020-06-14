package com.example.frybl.View.Activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.frybl.R;
import com.example.frybl.ViewModel.SignUpViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

import static com.example.frybl.View.Activities.MainActivity.component;

public class SignUpActivity extends AppCompatActivity {

    TextInputEditText nameField;
    TextInputEditText emailField;
    TextInputEditText passwordField;
    TextInputEditText confirmPasswordField;
    TextInputLayout nameFieldLayout;
    TextInputLayout emailFieldLayout;
    TextInputLayout passwordFieldLayout;
    TextInputLayout confirmPasswordLayout;
    SignUpViewModel viewModel;
    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLanguage();
        setContentView(R.layout.activity_sign_up);
        nameField = findViewById(R.id.signup_name_textField);
        emailField = findViewById(R.id.signup_email_textField);
        passwordField = findViewById(R.id.signup_password_textField);
        confirmPasswordField = findViewById(R.id.signup_confirm_password_textField);
        nameFieldLayout = findViewById(R.id.signup_name_layout);
        emailFieldLayout = findViewById(R.id.signup_email_layout);
        passwordFieldLayout = findViewById(R.id.signup_password_layout);
        confirmPasswordLayout = findViewById(R.id.signup_confirm_password_layout);
        signUpButton = findViewById(R.id.signup_signup_button);

        viewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        component.inject(viewModel);

        viewModel.getCurrentUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null)
                {
                    finish();
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        viewModel.getNameError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                nameFieldLayout.setError(s);
            }
        });

        viewModel.getEmailError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                emailFieldLayout.setError(s);
            }
        });

        viewModel.getPasswordError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                passwordFieldLayout.setError(s);
            }
        });

        viewModel.getConfirmPasswordError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                confirmPasswordLayout.setError(s);
            }
        });

    }

    public void signUp()
    {
        viewModel.signUpUser(
                nameField.getText().toString()
                ,emailField.getText().toString()
                ,passwordField.getText().toString()
                ,confirmPasswordField.getText().toString());
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
