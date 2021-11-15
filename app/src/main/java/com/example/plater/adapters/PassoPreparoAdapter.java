package com.example.plater.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plater.PassoPreparo;
import com.example.plater.R;
import com.example.plater.holders.MyViewHolder;

import java.util.List;

public class PassoPreparoAdapter extends RecyclerView.Adapter{

    Context context;
    List<PassoPreparo> modoPreparo;

    public PassoPreparoAdapter(Context context, List<PassoPreparo> modoPreparo) {
        this.context = context;
        this.modoPreparo = modoPreparo;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.tutorial_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CheckBox cbPassoPreparo = holder.itemView.findViewById(R.id.cbPassoPreparo);
        cbPassoPreparo.setText(modoPreparo.get(position).getInstrucao());
    }

    @Override
    public int getItemCount() {
        return modoPreparo.size();
    }
}
