package com.example.plater.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plater.Ingrediente;
import com.example.plater.R;
import com.example.plater.holders.MyViewHolder;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter{

    Context context;
    List<Ingrediente> ingredientes;

    public IngredientsAdapter(Context context, List<Ingrediente> ingredientes) {
        this.context = context;
        this.ingredientes = ingredientes;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.ingredient_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextView tvIngredientItem = holder.itemView.findViewById(R.id.tvIngredientItem);
        String ingrediente = ingredientes.get(position).getQuantidade() + " " + ingredientes.get(position).getUnidadeMedida() + " " + ingredientes.get(position).getInsumo();
        tvIngredientItem.setText(ingrediente);
    }

    @Override
    public int getItemCount() {
        return this.ingredientes.size();
    }
}
