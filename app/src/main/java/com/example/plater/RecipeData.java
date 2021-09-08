package com.example.plater;

import android.graphics.Bitmap;

import java.util.List;

public class RecipeData {

    public Bitmap image;
    public String title;
    public String description;
    public String userName;
    public int rendimento;
    public String tempoPreparo;
    public List<String> ingredients;
    public List<String> modoPreparo;

    public RecipeData(Bitmap image, String title, String description, String userName, int rendimento, String tempoPreparo, List<String> ingredients, List<String> modoPreparo) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.userName = userName;
        this.rendimento = rendimento;
        this.tempoPreparo = tempoPreparo;
        this.ingredients = ingredients;
        this.modoPreparo = modoPreparo;
    }
}
