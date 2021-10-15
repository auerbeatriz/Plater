package com.example.plater;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class RecipeBookViewModel extends ViewModel {

    List<RecipeData> recipeDataList = new ArrayList<>();

    public RecipeBookViewModel() {
        RecipeData recipe1 = new RecipeData(R.drawable.bolinho_bacalhau, "Bolinho de bacalhau", "Nós particularmente gostamos muito de salsinha e cebolinho, então caprichamos nos temperos.", "plater_chef");
        RecipeData recipe2 = new RecipeData(R.drawable.brownie, "Brownie", "Mesmo indo chocolate ao leite, não achei que ficou tão doce, ficou na medida.", "plater_chef");

        recipeDataList.add(recipe1);
        recipeDataList.add(recipe2);
    }

    public List<RecipeData> getRecipeDataList() {
        return recipeDataList;
    }
}
