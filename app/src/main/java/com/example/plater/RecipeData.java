package com.example.plater;

import java.util.List;

public class RecipeData {

    int image;
    public String title;
    public String description;
    public String userName;
    public int rendimento;
    public String tempoPreparo;
    public List<String> ingredients;
    public List<String> modoPreparo;

    public RecipeData(int image, String title, String description, String userName) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.userName = userName;
    }
}
