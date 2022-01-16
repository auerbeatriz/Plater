package com.example.plater.models;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.plater.Recipe;
import com.example.plater.utils.MyDB;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryRecipesViewModel extends AndroidViewModel {

    MutableLiveData<List<Recipe>> recipes;
    MyDB db;

    public CategoryRecipesViewModel(@NonNull Application application) {
        super(application);
        db = MyDB.getDatabase(application);
    }

    public LiveData<List<Recipe>> getCategoryRecipes(int idCategory) {
        if(recipes == null) {
            recipes = new MutableLiveData<List<Recipe>>();
            loadRecipes(idCategory);
        }
        return recipes;
    }

    private void loadRecipes(int idCategory) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                recipes.postValue(db.myDao().getCategoryRecipes(idCategory));
            }
        });
    }
}
