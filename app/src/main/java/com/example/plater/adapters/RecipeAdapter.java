package com.example.plater.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plater.R;
import com.example.plater.Recipe;
import com.example.plater.activities.RecipeDisplayActivity;
import com.example.plater.holders.MyViewHolder;
import com.example.plater.utils.HttpRequest;
import com.example.plater.utils.ImageCache;
import com.example.plater.utils.Util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecipeAdapter extends RecyclerView.Adapter {

    Activity context;
    List<Recipe> recipeList;

    public RecipeAdapter(Activity context, List<Recipe> recipeList) {
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

        String url;
        if(recipe.getUrlType().equals("t")) {
            url = recipe.getMediaUrl();
        } else {
            url = "http://img.youtube.com/vi/" + recipe.getMediaUrl() + "/default.jpg";
        }

        ImageView imageView = holder.itemView.findViewById(R.id.imvRecipeImage);
        ImageCache.loadToImageView(context, String.valueOf(recipe.getId()), imageView, url);

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
