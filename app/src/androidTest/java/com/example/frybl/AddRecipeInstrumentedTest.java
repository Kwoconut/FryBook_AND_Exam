package com.example.frybl;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.frybl.View.Activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AddRecipeInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void test_add_recipe_clicked() {
        onView(withId(R.id.floating_button_add_recipe)).perform(click());
        onView(withId(R.id.details_next_button)).check(matches(isDisplayed()));
    }

    @Test
    public void test_add_recipe_without_image_ingredients_and_instructions()
    {
        onView(withId(R.id.floating_button_add_recipe)).perform(click());
        onView(withId(R.id.details_recipe_name)).check(matches(isDisplayed()));
        onView(withId(R.id.details_recipe_name_textField)).perform(typeText("Cookies"));
        onView(withId(R.id.details_selector)).perform(typeText("Dessert"));
        onView(withId(R.id.details_recipe_preparationTime_textField)).perform(click());
        onView(withId(R.id.details_recipe_preparationTime_textField)).perform(typeText("20"));
        onView(withId(R.id.details_recipe_cookTime_textField)).perform(typeText("20"));
        onView(withId(R.id.instructions_fragment_button)).perform(click());
        onView(withId(R.id.add_instructions_save_button)).perform(click());
        onView(withText(R.string.error_add_recipe_text)).check(matches(isDisplayed()));
    }


    //get stuck on dialogfragment due to espresso not working with dialog fragments;
    @Test
    public void test_add_recipe_igredients_and_delete()
    {
        onView(withId(R.id.floating_button_add_recipe)).perform(click());
        onView(withId(R.id.details_recipe_name)).check(matches(isDisplayed()));
        onView(withId(R.id.ingredients_fragment_button)).perform(click());
        onView(withId(R.id.add_ingredients_floatingButton)).perform(click());
        onView(withId(R.id.dialogIngredientNameTextField)).inRoot(isDialog()).perform(typeText("salt"));
        onView(withId(R.id.dialogIngredientQuantityTextField)).inRoot(isDialog()).perform(typeText("2 tbsp"));
        onView(withText(R.string.add_ingredient)).inRoot(isDialog()).perform(click());
        onView(withId(R.id.add_ingredients_recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0 ,swipeRight()));
    }
}
