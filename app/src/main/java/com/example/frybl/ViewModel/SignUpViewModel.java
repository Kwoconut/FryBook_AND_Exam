package com.example.frybl.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.frybl.R;
import com.example.frybl.Repository.AppRepository;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

public class SignUpViewModel extends AndroidViewModel {

    @Inject
    AppRepository repository;

    private MutableLiveData<String> nameError;
    private MutableLiveData<String> emailError;
    private MutableLiveData<String> passwordError;
    private MutableLiveData<String> confirmPasswordError;

    public SignUpViewModel(@NonNull Application application) {
        super(application);
        nameError = new MutableLiveData<>();
        emailError = new MutableLiveData<>();
        passwordError = new MutableLiveData<>();
        confirmPasswordError = new MutableLiveData<>();
    }

    public LiveData<String> getNameError()
    {
        return nameError;
    }
    public LiveData<String> getEmailError() {
        return emailError;
    }

    public LiveData<String> getPasswordError() {
        return passwordError;
    }

    public LiveData<String> getConfirmPasswordError(){
        return confirmPasswordError;
    }

    public void signUpUser(String name,String email,String password,String confirmPassword)
    {
        if (isNameValid(name) && isEmailValid(email) && isPasswordValid(password) && isConfirmPasswordValid(password, confirmPassword))
        {
            repository.registerUser(name,email,password);
        }
    }

    private boolean isConfirmPasswordValid(String password,String confirmPassword) {
        if (!confirmPassword.equals(password))
        {
            confirmPasswordError.setValue("Passwords don't match");
            return false;
        }
        else
        {
            confirmPasswordError.setValue(null);
            return true;
        }
    }

    private boolean isNameValid(String name)
    {
        if (name == null || name.equals(""))
        {
            nameError.setValue(getApplication().getString(R.string.field_required));
            return false;
        }
        else if (name.length() <= 2)
        {
            nameError.setValue(getApplication().getString(R.string.user_name_less_than_2));
            return false;
        }
        else
        {
            nameError.setValue(null);
            return true;
        }
    }

    private boolean isEmailValid(String email)
    {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
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

    private boolean isPasswordValid(String password)
    {
        if (password.trim().equals("") || password == null)
        {
            passwordError.setValue(getApplication().getString(R.string.field_required));
            return false;
        }
        else if (password.length() < 6)
        {
            passwordError.setValue(getApplication().getString(R.string.password_less_than_6));
            return false;
        }
        else
        {
            passwordError.setValue(null);
            return true;
        }
    }

    public LiveData<FirebaseUser> getCurrentUser() {
        return repository.getCurrentUser();
    }
}
