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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frybl.Model.Ingredient;
import com.example.frybl.R;
import com.example.frybl.View.Adapters.IngredientsAdapter;
import com.example.frybl.ViewModel.SavedRecipeDetailsViewModel;

import java.util.List;

public class SavedRecipeDetailsIngredientsFragment extends Fragment {

    private RecyclerView recyclerView;
    private IngredientsAdapter ingredientsAdapter;
    private SavedRecipeDetailsViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(SavedRecipeDetailsViewModel.class);
        viewModel.getIngredientLiveData().observe(getActivity(), new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(List<Ingredient> ingredients) {
                ingredientsAdapter.setIngredientList(ingredients);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_upload_details_ingredients,container,false);


        ingredientsAdapter = new IngredientsAdapter();
        recyclerView = v.findViewById(R.id.upload_details_ingredients);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(ingredientsAdapter);

        return v;
    }


}
