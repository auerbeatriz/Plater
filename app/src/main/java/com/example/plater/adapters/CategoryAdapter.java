package com.example.plater.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plater.Category;
import com.example.plater.R;
import com.example.plater.activities.CategoryRecipesActivity;
import com.example.plater.holders.MyViewHolder;
import com.example.plater.utils.ImageCache;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter {
    Activity context;
    List<Category> categories;

    public CategoryAdapter(Activity context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.category_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Category category = categories.get(position);

        ImageView imvCategory = holder.itemView.findViewById(R.id.imvCategory);
        ImageCache.loadToImageView(context, category.getNome(), imvCategory, category.getImageUrl());

        imvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, CategoryRecipesActivity.class);
                i.putExtra("idCategoria", category.getId());
                context.startActivity(i);
            }
        });

        TextView tvCategory = holder.itemView.findViewById(R.id.tvCategoryName);
        tvCategory.setText(category.getNome());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
