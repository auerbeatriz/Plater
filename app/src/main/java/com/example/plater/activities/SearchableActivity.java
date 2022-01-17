package com.example.plater.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import com.example.plater.R;
import com.example.plater.Recipe;
import com.example.plater.adapters.RecipeAdapter;
import com.example.plater.models.MainActivityViewModel;
import com.example.plater.utils.MyDB;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchableActivity extends AppCompatActivity {

    MutableLiveData<List<Recipe>> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(SearchableActivity.this, 2);
            RecyclerView rvRecipes = findViewById(R.id.rvSearch);
            rvRecipes.setLayoutManager(gridLayoutManager);

            String query = intent.getStringExtra(SearchManager.QUERY);
            this.recipes = new MutableLiveData<List<Recipe>>();
            doMySearch(query);

            recipes.observe(SearchableActivity.this, new Observer<List<Recipe>>() {
                @Override
                public void onChanged(List<Recipe> recipes) {
                    RecipeAdapter recipeAdapter = new RecipeAdapter(SearchableActivity.this, recipes);
                    rvRecipes.setAdapter(recipeAdapter);
                }
            });
        }
    }

    private void doMySearch(String query) {
        MyDB db = MyDB.getDatabase(SearchableActivity.this);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                recipes.postValue(db.myDao().searchRecipes(query));
            }
        });
    }
}