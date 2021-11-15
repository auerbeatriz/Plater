package com.example.plater.activities;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;


import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.plater.Ingrediente;
import com.example.plater.PassoPreparo;
import com.example.plater.R;
import com.example.plater.Recipe;
import com.example.plater.adapters.IngredientsAdapter;
import com.example.plater.adapters.PassoPreparoAdapter;
import com.example.plater.models.MainActivityViewModel;
import com.example.plater.models.RecipeDisplayViewModel;

import java.util.List;

public class RecipeDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_display);

        //  obtendo qual foi a receita selecionada
        Intent i = getIntent();
        Recipe recipe = (Recipe) i.getSerializableExtra("recipe");

        /* elementos de interface */

        Toolbar toolbar = findViewById(R.id.tb_RecipeName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //  setando recyclerview de ingredientes e modo de preparo
        RecyclerView rvIngredientes = findViewById(R.id.rvIngredientes);
        rvIngredientes.setHasFixedSize(true);

        RecyclerView rvModoPreparo = findViewById(R.id.rvModoPreparo);
        rvModoPreparo.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManagerIngredients = new LinearLayoutManager(RecipeDisplayActivity.this, LinearLayoutManager.VERTICAL, false);
        rvIngredientes.setLayoutManager(linearLayoutManagerIngredients);

        LinearLayoutManager linearLayoutManagerTutorial = new LinearLayoutManager(RecipeDisplayActivity.this, LinearLayoutManager.VERTICAL, false);
        rvModoPreparo.setLayoutManager(linearLayoutManagerTutorial);

        //  preenchendo os dados da entidade INGREDIENTE
        //  ontendo viewmodel
        RecipeDisplayViewModel recipeDisplayViewModel = new ViewModelProvider(this, new RecipeDisplayViewModel.RecipeDisplayViewModelFactory(recipe.getId())).get(RecipeDisplayViewModel.class);

        LiveData<List<Ingrediente>> ingredientes = recipeDisplayViewModel.getIngredients();
        ingredientes.observe(this, new Observer<List<Ingrediente>>() {
            @Override
            public void onChanged(List<Ingrediente> ingredientes) {
                IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(RecipeDisplayActivity.this, ingredientes);
                rvIngredientes.setAdapter(ingredientsAdapter);
            }
        });

        LiveData<List<PassoPreparo>> modoPreparo = recipeDisplayViewModel.getModoPreparo();
        modoPreparo.observe(this, new Observer<List<PassoPreparo>>() {
            @Override
            public void onChanged(List<PassoPreparo> passoPreparos) {
                PassoPreparoAdapter passoPreparoAdapter = new PassoPreparoAdapter(RecipeDisplayActivity.this, passoPreparos);
                rvModoPreparo.setAdapter(passoPreparoAdapter);
            }
        });

        //  preenchendo os dados da receita da entidade RECEITA
        TextView tvTempoPreparo = findViewById(R.id.tvTempoPreparo);
        String tempoPreparo = recipe.getTempoPreparo() + " minutos";
        tvTempoPreparo.setText(tempoPreparo);

        TextView tvRendimento = findViewById(R.id.tvRendimento);
        String rendimento = recipe.getRendimento() + " " + recipe.getTipoRendimento();
        tvRendimento.setText(rendimento);

        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(recipe.getTitulo());

        TextView tvDescription = findViewById(R.id.tvDescription);
        tvDescription.setText(recipe.getDescricao());
    }
}