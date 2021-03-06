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

import com.example.frybl.Model.Ingredient;
import com.example.frybl.R;
import com.example.frybl.ViewModel.AddRecipeViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class DialogAddIngredientFragment extends AppCompatDialogFragment {

    private TextInputEditText editTextIngredientName;
    private TextInputEditText editTextIngredientQty;
    private AddRecipeViewModel viewModel;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_ingredients_dialog,null);

        builder.setView(view).setTitle(R.string.add_ingredient).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editTextIngredientName.getText().toString();
                String qty = editTextIngredientQty.getText().toString();
                viewModel.addRecipeIngredient(new Ingredient(name,qty,false));
            }
        });

        editTextIngredientName = view.findViewById(R.id.dialogIngredientNameTextField);
        editTextIngredientQty = view.findViewById(R.id.dialogIngredientQuantityTextField);

        return builder.create();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(AddRecipeViewModel.class);
    }
}
