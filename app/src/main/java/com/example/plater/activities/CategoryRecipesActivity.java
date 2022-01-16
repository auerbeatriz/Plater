package com.example.plater.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.plater.R;
import com.example.plater.Recipe;
import com.example.plater.adapters.RecipeAdapter;
import com.example.plater.models.CategoryRecipesViewModel;

import java.util.List;

public class CategoryRecipesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_recipes);

        Intent ai = getIntent();
        int idCategory = ai.getIntExtra("idCategoria", 0);

        Toolbar toolbar = findViewById(R.id.tbCategoryRecipes);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView rvCategoryRecipes = findViewById(R.id.rvCategoryRecipes);

        GridLayoutManager layoutManager = new GridLayoutManager(CategoryRecipesActivity.this, 2);
        rvCategoryRecipes.setLayoutManager(layoutManager);

        CategoryRecipesViewModel categoryRecipesViewModel = new CategoryRecipesViewModel(getApplication());
        LiveData<List<Recipe>> recipes = categoryRecipesViewModel.getCategoryRecipes(idCategory);
        recipes.observe(CategoryRecipesActivity.this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                RecipeAdapter recipeAdapter = new RecipeAdapter(CategoryRecipesActivity.this, recipes);
                rvCategoryRecipes.setAdapter(recipeAdapter);
            }
        });
    }
}