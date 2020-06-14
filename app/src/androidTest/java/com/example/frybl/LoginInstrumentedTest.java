package com.example.frybl;


import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.frybl.View.Activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class LoginInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void SetUp()
    {
        FirebaseAuth.getInstance().signOut();
    }

    @Test
    public void test_wrong_log_in()
    {
        onView(withId(R.id.login_email_textField)).perform(typeText("test@test"));
        onView(withId(R.id.login_password_textField)).perform(typeText("123"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.login_login_button)).perform(click());
    }

    @Test
    public void login()
    {
        onView(withId(R.id.login_email_textField)).perform(typeText("test@test.dk"));
        onView(withId(R.id.login_password_textField)).perform(typeText("abcd1234"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.login_login_button)).perform(click());
    }

    @Test
    public void createAccount()
    {
        onView(withId(R.id.login_signup_button)).perform(click());
        onView(withId(R.id.signup_name_textField)).perform(typeText("test"));
        onView(withId(R.id.signup_email_textField)).perform(typeText("test@test.dk"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.signup_password_textField)).perform(typeText("abcd1234"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.signup_confirm_password_textField)).perform(typeText("abcd1234"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.signup_signup_button)).perform(click());
    }

}
