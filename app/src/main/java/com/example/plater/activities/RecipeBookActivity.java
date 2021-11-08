package com.example.plater.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.plater.R;
import com.example.plater.adapters.RBFragmentsAdapter;
import com.google.android.material.tabs.TabLayout;

public class RecipeBookActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    RBFragmentsAdapter rbFragmentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_book);

        //  inicializando componentes
        this.tabLayout = findViewById(R.id.tabLayout);
        this.viewPager2 = findViewById(R.id.viewPager);

        FragmentManager fragmentManager = getSupportFragmentManager();
        this.rbFragmentsAdapter = new RBFragmentsAdapter(fragmentManager, getLifecycle());
        viewPager2.setAdapter(rbFragmentsAdapter);

        //  criando abas
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_my_recipes));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_my_favorites));

        //  definindo comportamento de mudança de abas
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }
}