package com.example.plater.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.plater.utils.HttpRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                HttpRequest httpRequest = new HttpRequest(category.getImageUrl(), "GET", "UTF-8");
                try {
                    InputStream is = httpRequest.execute();
                    Bitmap img = BitmapFactory.decodeStream(is);
                    is.close();
                    httpRequest.finish();
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imvCategory.setImageBitmap(img);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

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
