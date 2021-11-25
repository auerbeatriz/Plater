package com.example.plater;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PassoPreparo {

    @PrimaryKey
    private int id;
    private String instrucao;
    private int idReceita;

    public PassoPreparo(int id, String instrucao, int idReceita) {
        this.id = id;
        this.instrucao = instrucao;
        this.idReceita = idReceita;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getInstrucao() { return instrucao; }

    public void setInstrucao(String instrucao) { this.instrucao = instrucao; }

    public int getIdReceita() { return idReceita; }

    public void setIdReceita(int idReceita) { this.idReceita = idReceita; }
}
