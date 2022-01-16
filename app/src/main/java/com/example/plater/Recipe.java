package com.example.plater;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Recipe implements Serializable {

    @PrimaryKey
    private int id;
    private String titulo;
    private String descricao;
    private String tempoPreparo;
    private int rendimento;
    private String tipoRendimento;
    private int idCategoria;
    private String categoria;
    private String userName = "plater_chef";
    private String mediaUrl;
    private String urlType;

    public Recipe(int id, String titulo, String descricao, String tempoPreparo, int rendimento, String tipoRendimento, int idCategoria, String categoria, String mediaUrl, String urlType) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.tempoPreparo = tempoPreparo;
        this.rendimento = rendimento;
        this.tipoRendimento = tipoRendimento;
        this.idCategoria = idCategoria;
        this.categoria = categoria;
        this.userName = userName;
        this.mediaUrl = mediaUrl;
        this.urlType = urlType;
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

    public int getIdCategoria() { return idCategoria; }

    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

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

    public String getMediaUrl() { return mediaUrl; }

    public void setMediaUrl(String mediaUrl) { this.mediaUrl = mediaUrl; }

    public String getUrlType() { return urlType; }

    public void setUrlType(String urlType) { this.urlType = urlType; }
}
