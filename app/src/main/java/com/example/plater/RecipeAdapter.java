package com.example.plater;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.internal.VisibilityAwareImageButton;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter {

    Context context;
    List<RecipeData> recipeDataList;

    public RecipeAdapter(Context context, List<RecipeData> recipeDataList) {
        this.context = context;
        this.recipeDataList = recipeDataList;
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
        RecipeData recipeData = recipeDataList.get(position);

        ImageView imageView = holder.itemView.findViewById(R.id.imv_recipeImage);
        imageView.setImageResource(recipeData.image);

        TextView tvTitle = holder.itemView.findViewById(R.id.tv_recipeTitle);
        tvTitle.setText(recipeData.title);

        TextView tvDescription = holder.itemView.findViewById(R.id.tv_recipeDescription);
        tvDescription.setText(recipeData.description);

        TextView tvCreator = holder.itemView.findViewById(R.id.tv_recipeUserCreator);
        tvCreator.setText(recipeData.userName);

        //quando essa foto for selecionada
        CardView cardView = holder.itemView.findViewById(R.id.cvRecipe);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.itemView.getContext(), RecipeDisplay.class);
                holder.itemView.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.recipeDataList.size();
    }
}
