package com.example.plater;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Ingrediente {
    @PrimaryKey
    private int id;
    private String quantidade;
    private String unidadeMedida;
    private String insumo;
    private int idReceita;

    public Ingrediente(int id, String quantidade, String unidadeMedida, String insumo, int idReceita) {
        this.id = id;
        this.quantidade = quantidade;
        this.unidadeMedida = unidadeMedida;
        this.insumo = insumo;
        this.idReceita = idReceita;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getQuantidade() { return quantidade; }

    public void setQuantidade(String quantidade) { this.quantidade = quantidade; }

    public String getUnidadeMedida() { return unidadeMedida; }

    public void setUnidadeMedida(String unidadeMedida) { this.unidadeMedida = unidadeMedida; }

    public String getInsumo() { return insumo; }

    public void setInsumo(String insumo) { this.insumo = insumo; }

    public int getIdReceita() { return idReceita; }

    public void setIdReceita(int idReceita) { this.idReceita = idReceita; }
}
