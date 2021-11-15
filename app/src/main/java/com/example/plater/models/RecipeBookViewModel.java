package com.example.plater.models;

import androidx.lifecycle.ViewModel;

import com.example.plater.R;
import com.example.plater.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeBookViewModel extends ViewModel {

    List<Recipe> recipeList = new ArrayList<>();

    /*
    public RecipeBookViewModel() {
        Recipe recipe1 = new Recipe(R.drawable.bolinho_bacalhau, "Bolinho de bacalhau", "Nós particularmente gostamos muito de salsinha e cebolinho, então caprichamos nos temperos.", "plater_chef");
        Recipe recipe2 = new Recipe(R.drawable.brownie, "Brownie", "Mesmo indo chocolate ao leite, não achei que ficou tão doce, ficou na medida.", "plater_chef");

        recipeList.add(recipe1);
        recipeList.add(recipe2);
    }*/

    public List<Recipe> getRecipeList() {
        return recipeList;
    }
}
