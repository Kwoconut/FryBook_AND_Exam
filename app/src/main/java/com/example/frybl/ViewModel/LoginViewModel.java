package com.example.frybl.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.frybl.Model.Quote;
import com.example.frybl.R;
import com.example.frybl.Repository.AppRepository;

import javax.inject.Inject;

public class LoginViewModel extends AndroidViewModel {

    @Inject
    AppRepository repository;

    private MutableLiveData<String> emailError;
    private MutableLiveData<String> passwordError;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        emailError = new MutableLiveData<>();
        passwordError = new MutableLiveData<>();
    }

    public LiveData<String> getEmailError() {
        return emailError;
    }

    public LiveData<String> getPasswordError() {
        return passwordError;
    }

    public LiveData<Boolean> isErrorLogin()
    {
        return repository.isErrorLogin();
    }

    public void loginUser(String email,String password)
    {
        if (isEmailValid(email) && isPasswordValid(password))
        {
            repository.loginUser(email,password);
        }
    }

    public boolean isEmailValid(String email)
    {
        if (android.util
                .Patterns
                .EMAIL_ADDRESS
                .matcher(email)
                .matches())
        {
            emailError.setValue(null);
            return true;
        }
        else
        {
            emailError.setValue(getApplication().getString(R.string.invalid_email));
            return false;
        }
    }

    public boolean isPasswordValid(String password)
    {
        if (password == null || password.trim().equals(""))
        {
            passwordError.setValue(getApplication().getString(R.string.field_required));
            return false;
        }
        else if (password.length() < 6)
        {
            passwordError.setValue(getApplication().getString(R.string.invalid_password));
            return false;
        }
        else
        {
            passwordError.setValue("");
            return true;
        }
    }

    public LiveData<Quote> getQuote()
    {
        return repository.getQuote();
    }

    public void requestQuote() {
        repository.requestQuote();
    }
}
