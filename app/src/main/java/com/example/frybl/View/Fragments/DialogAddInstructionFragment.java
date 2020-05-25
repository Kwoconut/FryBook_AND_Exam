package com.example.frybl.View.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.frybl.Model.Instruction;
import com.example.frybl.R;
import com.example.frybl.ViewModel.AddRecipeViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class DialogAddInstructionFragment extends AppCompatDialogFragment {

    private TextInputEditText editTextInstructionName;
    private AddRecipeViewModel viewModel;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_instructions_dialog,null);

        builder.setView(view).setTitle("Add instruction").setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editTextInstructionName.getText().toString();
                viewModel.addRecipeInstruction(new Instruction(name,false));
            }
        });

        editTextInstructionName = view.findViewById(R.id.dialogInstructionNameTextField);

        return builder.create();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(AddRecipeViewModel.class);
    }
}
