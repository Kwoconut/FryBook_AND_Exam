package com.example.frybl.LocalDB.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.frybl.Model.Instruction;

import java.util.List;

@Dao
public interface InstructionDAO {

    @Insert
    void insert(Instruction instruction);

    @Insert
    void insertInstructions(List<Instruction> instructions);

    @Query("SELECT * FROM instruction_table WHERE recipeId = :id")
    LiveData<List<Instruction>> getAllInstructionsByRecipeId(String id);

    @Query("DELETE FROM instruction_table where recipeId = :id")
    void deleteInstructionsByRecipeId(String id);

    @Query("DELETE FROM instruction_table")
    void deleteAllInstructions();
}
