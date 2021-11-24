package com.example.plater;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plater.activities.RecipeDisplayActivity;
import com.example.plater.holders.MyViewHolder;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter {

    Context context;
    List<Recipe> recipeList;

    public RecipeAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recipe_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);

        ImageView imageView = holder.itemView.findViewById(R.id.imvRecipeImage);
        imageView.setImageResource(recipe.getImage());

        TextView tvTitle = holder.itemView.findViewById(R.id.tvRecipeTitle);
        tvTitle.setText(recipe.getTitulo());

        TextView tvDescription = holder.itemView.findViewById(R.id.tvRecipeDescription);
        tvDescription.setText(recipe.getDescricao());

        TextView tvCreator = holder.itemView.findViewById(R.id.tvRecipeUserCreator);
        tvCreator.setText(recipe.getUserName());

        //quando a receita for selecionada
        CardView cardView = holder.itemView.findViewById(R.id.cvRecipe);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, RecipeDisplayActivity.class);
                i.putExtra("recipe", recipe);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.recipeList.size();
    }
}
