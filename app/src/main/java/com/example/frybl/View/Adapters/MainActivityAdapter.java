package com.example.frybl.View.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frybl.Model.Upload;
import com.example.frybl.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class MainActivityAdapter extends FirestoreRecyclerAdapter<Upload, MainActivityAdapter.ViewHolder> {

    private OnItemClickListener listener;

    public MainActivityAdapter(@NonNull FirestoreRecyclerOptions<Upload> options) {
        super(options);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_feed_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Upload model) {
        holder.recipeTitleTextView.setText(model.getRecipe().getName());
        holder.recipeCookTimeTextView.setText(model.getRecipe().getCookTime() + " minutes");
        holder.recipePrepTimeTextView.setText(model.getRecipe().getPreparationTime() + " minutes");
        holder.recipeRatingBar.setRating(model.getRating());
        Picasso.get().load(model.getRecipe().getImageUri()).placeholder(R.mipmap.ic_launcher_round).fit().centerCrop().into(holder.recipeImage);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }

    public interface OnItemClickListener
    {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView recipeTitleTextView;
        TextView recipePrepTimeTextView;
        TextView recipeCookTimeTextView;
        RatingBar recipeRatingBar;
        ImageView recipeImage;
        Button moreButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeTitleTextView = itemView.findViewById(R.id.recipe_feed_title);
            recipeCookTimeTextView = itemView.findViewById(R.id.recipe_feed_cookTime_value);
            recipePrepTimeTextView = itemView.findViewById(R.id.recipe_feed_prepTime_value);
            recipeRatingBar = itemView.findViewById(R.id.recipe_feed_rating_bar);
            recipeImage = itemView.findViewById(R.id.recipe_feed_image);
            moreButton = itemView.findViewById(R.id.recipe_feed_button);

            moreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null)
                    {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }


    }
}
