package com.example.frybl.View.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frybl.Model.Ingredient;
import com.example.frybl.R;

import java.util.ArrayList;
import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientHolder> {

    private List<Ingredient> ingredientList;

    public IngredientsAdapter()
    {
        ingredientList = new ArrayList<>();
    }

    @NonNull
    @Override
    public IngredientsAdapter.IngredientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_add_ingredients_item,parent,false);
        return new IngredientsAdapter.IngredientHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsAdapter.IngredientHolder holder, int position) {
        Ingredient currentIngredient = ingredientList.get(position);
        holder.ingredientItemName.setText(currentIngredient.getName());
        holder.ingredientItemQty.setText(currentIngredient.getQuantity());

    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public Ingredient getIngredientAt(int position)
    {
        return ingredientList.get(position);
    }

    public void setIngredientList(List<Ingredient> ingredientList)
    {
        this.ingredientList = ingredientList;
        notifyDataSetChanged();
    }


    class IngredientHolder extends RecyclerView.ViewHolder
    {
        TextView ingredientItemName;
        TextView ingredientItemQty;
        public IngredientHolder(@NonNull View itemView) {
            super(itemView);
            ingredientItemName = itemView.findViewById(R.id.ingredient_item_name);
            ingredientItemQty = itemView.findViewById(R.id.ingredient_item_quantity);
        }

    }
}
