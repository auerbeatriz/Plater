package com.example.plater;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

@Entity
public class Recipe implements Serializable {

    @PrimaryKey
    private int id;
    private String titulo;
    private String descricao;
    private String tempoPreparo;
    private int rendimento;
    private String tipoRendimento;
    private String categoria;
    private String userName = "plater_chef";
    private int image = R.drawable.img_wafflefix;

    public Recipe(int id, String titulo, String descricao, String tempoPreparo, int rendimento, String tipoRendimento, String categoria) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.tempoPreparo = tempoPreparo;
        this.rendimento = rendimento;
        this.tipoRendimento = tipoRendimento;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTempoPreparo() {
        return tempoPreparo;
    }

    public void setTempoPreparo(String tempoPreparo) {
        this.tempoPreparo = tempoPreparo;
    }

    public int getRendimento() {
        return rendimento;
    }

    public void setRendimento(int rendimento) {
        this.rendimento = rendimento;
    }

    public String getTipoRendimento() {
        return tipoRendimento;
    }

    public void setTipoRendimento(String tipoRendimento) {
        this.tipoRendimento = tipoRendimento;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
