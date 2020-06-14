package com.example.frybl.View.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.frybl.Model.Quote;
import com.example.frybl.R;
import com.example.frybl.ViewModel.LoginViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;

import static com.example.frybl.View.Activities.MainActivity.component;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText emailField;
    TextInputEditText passwordField;
    TextInputLayout emailLayout;
    TextInputLayout passwordLayout;
    TextView quoteText;
    Button loginButton;
    Button signupButton;
    LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLanguage();
        setContentView(R.layout.activity_login);
        emailField = findViewById(R.id.login_email_textField);
        passwordField = findViewById(R.id.login_password_textField);
        emailLayout = findViewById(R.id.login_email_layout);
        passwordLayout = findViewById(R.id.login_password_layout);
        loginButton = findViewById(R.id.login_login_button);
        signupButton = findViewById(R.id.login_signup_button);
        quoteText = findViewById(R.id.login_quote);


        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        component.inject(loginViewModel);
        loginViewModel.requestQuote();

        loginViewModel.getQuote().observe(this, new Observer<Quote>() {
            @Override
            public void onChanged(Quote quote) {
                quoteText.setText(quote.getContent() + " - " + quote.getAuthor());
            }
        });

        loginViewModel.isErrorLogin().observe(this, new Observer<Boolean>() {

            @Override
            public void onChanged(Boolean isError) {
                if (!isError)
                {
                    finish();
                }
                else if (isError)
                {
                    emailLayout.setError(getString(R.string.invalid_email));
                    passwordLayout.setError(getString(R.string.invalid_password));
                }
            }
        });

        loginViewModel.getEmailError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                emailLayout.setError(s);
            }
        });

        loginViewModel.getPasswordError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                passwordLayout.setError(s);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
            }
        });
    }

    public void loginUser()
    {
        loginViewModel.loginUser(emailField.getText().toString(),passwordField.getText().toString());
    }

    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
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
