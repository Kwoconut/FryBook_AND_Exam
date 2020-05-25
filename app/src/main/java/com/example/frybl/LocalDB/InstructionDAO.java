package com.example.frybl.LocalDB;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.frybl.Model.Instruction;

import java.util.List;

@Dao
public interface InstructionDAO {

    @Insert
    void insert(Instruction instruction);

    @Insert
    void insertInstructions(List<Instruction> instructions);

}
