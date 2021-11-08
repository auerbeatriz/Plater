package com.example.plater.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plater.holders.MyViewHolder;
import com.example.plater.R;

import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter {

    Context context;
    List<Integer> filterIconsList;
    List<Integer> filterIconsSelectedList;

    public FilterAdapter(Context context, List<Integer> filterIconsList, List<Integer> filterIconsSelectedList) {
        this.context = context;
        this.filterIconsList = filterIconsList;
        this.filterIconsSelectedList = filterIconsSelectedList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.filter_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ImageView imageView = holder.itemView.findViewById(R.id.imvFilter);
        imageView.setImageResource(filterIconsList.get(position));
        imageView.setTag(filterIconsList.get(position));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageView.getTag() == filterIconsList.get(position)) {
                    imageView.setImageResource(filterIconsSelectedList.get(position));
                    imageView.setTag(filterIconsSelectedList.get(position));
                }
                else {
                    imageView.setImageResource(filterIconsList.get(position));
                    imageView.setTag(filterIconsList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.filterIconsList.size();
    }
}
