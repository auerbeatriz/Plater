package com.example.plater;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewModel extends ViewModel {
    List<RecipeData> recipeDataList = new ArrayList<>();

    public List<RecipeData> getRecipeDataList() {
        return recipeDataList;
    }
}
