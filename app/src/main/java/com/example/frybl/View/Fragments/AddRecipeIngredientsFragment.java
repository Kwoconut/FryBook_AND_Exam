package com.example.frybl.View.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frybl.View.Adapters.AddRecipeIngredientsAdapter;
import com.example.frybl.Model.Ingredient;
import com.example.frybl.R;
import com.example.frybl.ViewModel.AddRecipeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class AddRecipeIngredientsFragment extends Fragment {

    private RecyclerView ingredientsRV;
    private FloatingActionButton floatingActionButton;
    private AddRecipeViewModel viewModel;
    private AddRecipeIngredientsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_recipe_ingredients,container,false);

        ingredientsRV = v.findViewById(R.id.add_ingredients_recyclerView);
        ingredientsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        ingredientsRV.setHasFixedSize(true);
        adapter = new AddRecipeIngredientsAdapter();
        ingredientsRV.setAdapter(adapter);

        floatingActionButton = v.findViewById(R.id.add_ingredients_floatingButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                viewModel.removeRecipeIngredient(adapter.getIngredientAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(ingredientsRV);

        return v;
    }


    public void openDialog()
    {
        DialogAddIngredientFragment dialog = new DialogAddIngredientFragment();
        dialog.show(getParentFragmentManager(),"Add ingredient");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(AddRecipeViewModel.class);
        viewModel.getRecipeIngredients().observe(getViewLifecycleOwner(), new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(List<Ingredient> ingredients) {
                adapter.setIngredientList(ingredients);
            }
        });

    }


}
