package com.example.frybl;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.frybl.Repository.AppRepository;
import com.example.frybl.ViewModel.LoginViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@RunWith(RobolectricTestRunner.class)
@Config(application = android.app.Application.class,manifest="src/main/AndroidManifest.xml")
public class LoginViewModelUnitTest {
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock Application application;
    @Mock AppRepository repository;
    @InjectMocks LoginViewModel loginViewModel;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_is_valid_email_without_at(){
        assertFalse(loginViewModel.isEmailValid("abcddasd"));
    }

    @Test
    public void test_is_valid_email()
    {
        assertTrue(loginViewModel.isEmailValid("test@test.dk"));
    }

    @Test
    public void test_is_valid_passowrd_when_null()
    {
        assertFalse(loginViewModel.isPasswordValid(null));
    }

    @Test
    public void test_is_valid_password_when_empty()
    {
        assertFalse(loginViewModel.isPasswordValid(""));
    }

    @Test
    public void check_for_right_error_when_password_empty()
    {
        loginViewModel.isPasswordValid("");
        assertEquals(application.getString(R.string.field_required),loginViewModel.getPasswordError().getValue());
    }

    @Test
    public void check_for_right_error_when_password_less_than_6()
    {
        loginViewModel.isPasswordValid("abcd");
        assertEquals(application.getString(R.string.invalid_password),loginViewModel.getPasswordError().getValue());
    }

    @Test
    public void test_is_password_less_than_6()
    {
        assertFalse(loginViewModel.isPasswordValid("abcd"));
    }

    @Test
    public void test_valid_password()
    {
        assertTrue(loginViewModel.isPasswordValid("abcd1234"));
    }



}