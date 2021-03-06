package com.example.frybl.Model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "instruction_table")
public class Instruction {

    @Ignore
    public static final String FIRESTOREKEY_INSTRUCTION_DESCRIPTION = "description";
    @Ignore
    public static final String FIRESTOREKEY_INSTRUCTION_RECIPEID = "recipeId";


    @PrimaryKey (autoGenerate = true)
    private int instructionId;
    private String description;
    private boolean ticked;
    private String recipeId;

    @Ignore
    public Instruction()
    {

    }

    public Instruction(String description, boolean ticked) {
        this.description = description;
        this.ticked = ticked;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public int getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(int instructionId) {
        this.instructionId = instructionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isTicked() {
        return ticked;
    }

    public void setTicked(boolean ticked) {
        this.ticked = ticked;
    }

}
