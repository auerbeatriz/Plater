package com.example.plater;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Categoria {
    @PrimaryKey
    private int id;
    private String nomeCategoria;

    public Categoria(int id, String nomeCategoria) {
        this.id = id;
        this.nomeCategoria = nomeCategoria;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getNomeCategoria() { return nomeCategoria; }

    public void setNomeCategoria(String nomeCategoria) { this.nomeCategoria = nomeCategoria; }
}
