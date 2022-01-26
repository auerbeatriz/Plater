package com.example.plater.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.plater.Recipe;
import com.example.plater.fragments.CategoriesFragment;
import com.example.plater.fragments.HomeFragment;
import com.example.plater.models.MainActivityViewModel;
import com.example.plater.R;
import com.example.plater.fragments.RecipeBookFragment;
import com.example.plater.utils.Config;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Config.getLogin(MainActivity.this).isEmpty()) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        else {
            setContentView(R.layout.activity_main);

            //Integrando a nova toolbar com a activity
            Toolbar toolbar = findViewById(R.id.tb_main);
            setSupportActionBar(toolbar);

            MainActivityViewModel viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

            //Integrando o bottomNavigationView
            bottomNavigationView = findViewById(R.id.bnvMain);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    viewModel.setBottomViewNavigationOp(item.getItemId());

                    switch (item.getItemId()) {
                        case R.id.homeView:
                            HomeFragment homeFragment = HomeFragment.newInstance();
                            setFragment(homeFragment);
                            break;
                        case R.id.categoryView:
                            CategoriesFragment categoriesFragment = CategoriesFragment.newInstance();
                            setFragment(categoriesFragment);
                            break;
                        case R.id.recipeBookView:
                            RecipeBookFragment recipeBookFragment = RecipeBookFragment.newInstance();
                            setFragment(recipeBookFragment);
                            break;
                    }
                    return true;
                }
            });

            int bottomViewNavigationOp = viewModel.getBottomViewNavigationOp();
            bottomNavigationView.setSelectedItemId(bottomViewNavigationOp);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_toolbar, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.icSearch).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case R.id.icConfig:
                Intent i = new Intent(MainActivity.this, AccountConfigActivity.class);
                startActivity(i);
                return true;
            case R.id.icSearch:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}