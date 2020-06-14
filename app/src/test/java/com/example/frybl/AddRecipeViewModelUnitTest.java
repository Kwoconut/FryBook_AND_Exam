package com.example.frybl;

import android.app.Application;
import android.net.Uri;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.frybl.Model.Ingredient;
import com.example.frybl.Model.Instruction;
import com.example.frybl.Repository.AppRepository;
import com.example.frybl.ViewModel.AddRecipeViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@RunWith(MockitoJUnitRunner.class)
public class AddRecipeViewModelUnitTest {
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock Application application;
    @Mock AppRepository repository;
    @InjectMocks AddRecipeViewModel addRecipeViewModel;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_setter_for_recipe_name(){
        addRecipeViewModel.setRecipeName("asd");
        assertEquals("asd",addRecipeViewModel.getRecipeName().getValue());
    }

    @Test
    public void checkValidRecipeNameIfEmpty()
    {
        addRecipeViewModel.setRecipeName("");
        assertFalse(addRecipeViewModel.isValidName());
    }

    @Test
    public void checkValidRecipeNameIfLessThan3()
    {
        addRecipeViewModel.setRecipeName("asd");
        assertFalse(addRecipeViewModel.isValidName());
    }

    @Test
    public void checkValidRecipeName()
    {
        addRecipeViewModel.setRecipeName("Cookies on a roll");
        assertTrue(addRecipeViewModel.isValidName());
    }

    @Test
    public void checkValidCookTimeIfEmptyOr0()
    {
        assertFalse(addRecipeViewModel.isValidCookTime());
        addRecipeViewModel.setRecipeCookTime(0);
        assertFalse(addRecipeViewModel.isValidCookTime());
    }

    @Test
    public void checkValidCookTimeOver1000()
    {
        addRecipeViewModel.setRecipeCookTime(1000);
        assertFalse(addRecipeViewModel.isValidCookTime());
    }

    @Test
    public void checkValidPrepTimeIfEmptyOr0()
    {
        assertFalse(addRecipeViewModel.isValidPrepTime());
        addRecipeViewModel.setRecipePrepTime(0);
        assertFalse(addRecipeViewModel.isValidPrepTime());
    }

    @Test
    public void checkValidPrepTimeOver1000()
    {
        addRecipeViewModel.setRecipePrepTime(1000);
        assertFalse(addRecipeViewModel.isValidPrepTime());
    }

    @Test
    public void checkFieldsErrorExists()
    {
        addRecipeViewModel.checkFields();
        assertEquals(true,addRecipeViewModel.getIsError().getValue());
    }

    @Test
    public void checkFieldsWithDetailsCompleted()
    {
        addRecipeViewModel.setRecipeName("Cookies");
        addRecipeViewModel.setRecipePrepTime(5);
        addRecipeViewModel.setRecipeCookTime(10);
        addRecipeViewModel.checkFields();
        assertEquals(true,addRecipeViewModel.getIsError().getValue());
    }

    @Test
    public void checkFieldsWithDetailsAndImageCompleted()
    {
        addRecipeViewModel.setRecipeName("Cookies");
        addRecipeViewModel.setRecipePrepTime(5);
        addRecipeViewModel.setRecipeCookTime(10);
        addRecipeViewModel.setRecipeImageUri(Mockito.mock(Uri.class));
        addRecipeViewModel.checkFields();
        assertEquals(true,addRecipeViewModel.getIsError().getValue());
    }

    @Test
    public void checkCompletedFields()
    {
        addRecipeViewModel.setRecipeName("Cookies");
        addRecipeViewModel.setRecipePrepTime(5);
        addRecipeViewModel.setRecipeCookTime(10);
        addRecipeViewModel.setRecipeImageUri(Mockito.mock(Uri.class));
        addRecipeViewModel.addRecipeIngredient(Mockito.mock(Ingredient.class));
        addRecipeViewModel.addRecipeInstruction(Mockito.mock(Instruction.class));
        addRecipeViewModel.checkFields();
        assertEquals(false,addRecipeViewModel.getIsError().getValue());
    }

    @Test
    public void checkAddRecipe()
    {
        addRecipeViewModel.setRecipeName("Cookies");
        addRecipeViewModel.setRecipePrepTime(5);
        addRecipeViewModel.setRecipeCookTime(10);
        addRecipeViewModel.setRecipeImageUri(Mockito.mock(Uri.class));
        addRecipeViewModel.addRecipeIngredient(Mockito.mock(Ingredient.class));
        addRecipeViewModel.addRecipeInstruction(Mockito.mock(Instruction.class));
        addRecipeViewModel.addRecipe("jpg");
        assertFalse(addRecipeViewModel.getIsError().getValue());
    }



}