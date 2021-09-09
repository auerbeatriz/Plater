package com.example.plater;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewModel extends ViewModel {
    List<RecipeData> recipeDataList = new ArrayList<>();
    int bottomViewNavigationOp = R.id.homeView;

    public MainActivityViewModel() {
        RecipeData recipe1 = new RecipeData(R.drawable.bolinho_bacalhau, "Bolinho de bacalhau", "Nós particularmente gostamos muito de salsinha e cebolinho, então caprichamos nos temperos.", "plater_chef");
        RecipeData recipe2 = new RecipeData(R.drawable.brownie, "Brownie", "Mesmo indo chocolate ao leite, não achei que ficou tão doce, ficou na medida.", "plater_chef");
        RecipeData recipe3 = new RecipeData(R.drawable.cheesecake, "Cheesecake", "The crust is simply a variation of my basic brownie recipe baked right in a springform pan.", "plater_chef");
        RecipeData recipe4 = new RecipeData(R.drawable.cocconut_curry, "Cocconut Curry", "They’re seductively addictive, always well balanced and one of the world’s great cuisines. They’re also easy! This is a very simple dish to make." ,"plater_chef");
        RecipeData recipe5 = new RecipeData(R.drawable.enchilada_cups, "Enchilada cups", "They weren’t kidding when they named it March Madness.", "plater_chef");
        RecipeData recipe6 = new RecipeData(R.drawable.img_wafflefix, "Waffle americano", "Delícia", "plater_chef");

        recipeDataList.add(recipe1);
        recipeDataList.add(recipe2);
        recipeDataList.add(recipe3);
        recipeDataList.add(recipe4);
        recipeDataList.add(recipe5);
        recipeDataList.add(recipe6);
    }

    public List<RecipeData> getRecipeDataList() {
        return recipeDataList;
    }

    public int getBottomViewNavigationOp() {
        return bottomViewNavigationOp;
    }

    public void setBottomViewNavigationOp(int bottomViewNavigationOp) {
        this.bottomViewNavigationOp = bottomViewNavigationOp;
    }
}
