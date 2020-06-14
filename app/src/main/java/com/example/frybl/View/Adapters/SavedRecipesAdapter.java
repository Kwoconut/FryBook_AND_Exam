package com.example.frybl.View.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frybl.Model.RecipeMinimal;
import com.example.frybl.R;

public class SavedRecipesAdapter extends ListAdapter<RecipeMinimal,SavedRecipesAdapter.SavedRecipesHolder> {

    private static final DiffUtil.ItemCallback<RecipeMinimal> DIFF_CALLBACK = new DiffUtil.ItemCallback<RecipeMinimal>() {
        @Override
        public boolean areItemsTheSame(@NonNull RecipeMinimal oldItem, @NonNull RecipeMinimal newItem) {
            return oldItem.getRecipeId().equals(newItem.getRecipeId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull RecipeMinimal oldItem, @NonNull RecipeMinimal newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    };
    private OnItemClickListener listener;

    public SavedRecipesAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public SavedRecipesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_recipe_item,parent,false);
        return new SavedRecipesHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedRecipesHolder holder, int position) {
        RecipeMinimal recipe = getItem(position);
        holder.recipeNameTextView.setText(recipe.getName());
    }

    public RecipeMinimal getRecipeAt(int position)
    {
        return getItem(position);
    }

    public void setListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(String recipeId);
    }

    class SavedRecipesHolder extends RecyclerView.ViewHolder
    {
        private TextView recipeNameTextView;

        public SavedRecipesHolder(@NonNull View itemView) {
            super(itemView);
            recipeNameTextView = itemView.findViewById(R.id.saved_recipe_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION)
                        listener.onItemClick(getItem(position).getRecipeId());
                }
            });
        }
    }
}
