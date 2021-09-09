package com.example.plater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.logging.Filter;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Integrando a nova toolbar com a activity
        Toolbar toolbar = findViewById(R.id.tb_main);
        setSupportActionBar(toolbar);

        MainActivityViewModel viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        //Integrando o bottomNavigationView
        bottomNavigationView = findViewById(R.id.btmNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                viewModel.setBottomViewNavigationOp(item.getItemId());

                switch (item.getItemId()) {
                    case R.id.homeView:
                        HomeFragment homeFragment = HomeFragment.newInstance();
                        setFragment(homeFragment);
                        break;
                    case R.id.filterView:
                        FilterFragment filterFragment = FilterFragment.newInstance();
                        setFragment(filterFragment);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_toolbar, menu);
        return true;
    }

    void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}