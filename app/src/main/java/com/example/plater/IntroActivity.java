package com.example.plater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private ViewPager viewPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabIndicator;
    Button btnNext;
    Button btnGetStarted;
    int position = 0;
    Animation btnAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // make the activity full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // check if the user has already opened this activity before
        if(restorePrefData()) {
            Intent i = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }


        setContentView(R.layout.activity_intro);

        // hide the action bar
        getSupportActionBar().hide();

        //ini views
        btnNext = findViewById(R.id.btn_intro_next);
        btnGetStarted = findViewById(R.id.btn_get_started);
        tabIndicator = findViewById(R.id.tab_indicator);
        btnAnim = AnimationUtils.loadAnimation(IntroActivity.this,R.anim.button_animation);


        // fill list screen
        List<ScreenItem> itemsList = new ArrayList<>();
        itemsList.add(new ScreenItem("Descubra receitas", "Delicie-se com receitas compartilhadas por outros usuários na plataforma",R.drawable.img_um_xhdpi));
        itemsList.add(new ScreenItem("Cadastre uma despensa", "Uma despensa virtual que exibe e te ajuda a achar receitas a partir de ingredientes você possui em casa",R.drawable.img_dois_xhdpi));
        itemsList.add(new ScreenItem("Compartilhe receitas", "Compartilhe seus pratos preferidos com outros usuários ao adicionar receitas no Plater",R.drawable.img_tres_xhdpi));

        // setup viewpager
        viewPager = findViewById(R.id.vp_intro);
        introViewPagerAdapter = new IntroViewPagerAdapter(IntroActivity.this, itemsList);
        viewPager.setAdapter(introViewPagerAdapter);

        // setup tablayout with viewpager
        tabIndicator.setupWithViewPager(viewPager);

        // next button clickListener
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = viewPager.getCurrentItem();

                if (position < itemsList.size()) {
                    position ++;
                    viewPager.setCurrentItem(position);
                }
                if (position == itemsList.size() -1) {
                    loadLastScreen();
                }
            }
        });

        // tablayout add changelistener
        tabIndicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == itemsList.size() -1) {
                    loadLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // getstartedbutton onclicklistener
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open main activity
                Intent i = new Intent(IntroActivity.this,MainActivity.class);
                startActivity(i);

                savePrefereceData();
                finish();
            }
        });
    }

    private boolean restorePrefData() {
        SharedPreferences sp = IntroActivity.this.getSharedPreferences("myprefs", MODE_PRIVATE);
        return sp.getBoolean("IS_INTRO_OPENED", false);
    }

    private void savePrefereceData() {
        SharedPreferences sp = IntroActivity.this.getSharedPreferences("myprefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("IS_INTRO_OPENED",true);
        editor.apply();
    }

    private void loadLastScreen() {
        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);

        // setup animation
        btnGetStarted.setAnimation(btnAnim);
    }
}