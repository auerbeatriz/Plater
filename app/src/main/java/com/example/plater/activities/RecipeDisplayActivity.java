package com.example.plater.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plater.Ingrediente;
import com.example.plater.PassoPreparo;
import com.example.plater.R;
import com.example.plater.Recipe;
import com.example.plater.adapters.IngredientsAdapter;
import com.example.plater.adapters.PassoPreparoAdapter;
import com.example.plater.models.RecipeBookViewModel;
import com.example.plater.models.RecipeDisplayViewModel;
import com.example.plater.utils.Config;
import com.example.plater.utils.HttpRequest;
import com.example.plater.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecipeDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_display);

        //  obtendo qual foi a receita selecionada
        Intent i = getIntent();
        Recipe recipe = (Recipe) i.getSerializableExtra("recipe");

        final String email = Config.getLogin(RecipeDisplayActivity.this);
        final String senha = Config.getPassword(RecipeDisplayActivity.this);
        final String username = Config.getUsername(RecipeDisplayActivity.this);

        ImageView btnFavoritar = findViewById(R.id.btnFavoritar);
        btnFavoritar.setTag(R.drawable.ic_favoritar);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                HttpRequest httpRequest = new HttpRequest(Config.SERVER_URL_BASE + "verifica_se_receita_favorita.php", "POST", "UTF-8");
                httpRequest.setBasicAuth(email, senha);
                httpRequest.addParam("username", username);
                httpRequest.addParam("id_receita", String.valueOf(recipe.getId()));
                try {
                    InputStream is = httpRequest.execute();
                    String result = Util.inputStream2String(is, "UTF-8");
                    httpRequest.finish();

                    Log.d("HTTP_REQUEST_RESULT", result);

                    JSONObject jsonObject = new JSONObject(result);
                    final int success = jsonObject.getInt("success");
                    if(success == 1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnFavoritar.setTag(R.drawable.ic_favorito);
                                btnFavoritar.setImageResource(R.drawable.ic_favorito);
                            }
                        });
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

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
        RecipeDisplayViewModel recipeDisplayViewModel = new ViewModelProvider(this, new RecipeDisplayViewModel.RecipeDisplayViewModelFactory(this.getApplication(),recipe.getId())).get(RecipeDisplayViewModel.class);

        MutableLiveData<List<Ingrediente>> ingredientes = recipeDisplayViewModel.getIngredients();
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

        btnFavoritar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer resource = (Integer) btnFavoritar.getTag();
                if(resource == R.drawable.ic_favoritar) {
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            HttpRequest httpRequest = new HttpRequest(Config.SERVER_URL_BASE + "favorita_desfavorita.php", "POST", "UTF-8");
                            httpRequest.setBasicAuth(email, senha);
                            httpRequest.addParam("username", username);
                            httpRequest.addParam("opcao", "1");
                            httpRequest.addParam("id_receita", String.valueOf(recipe.getId()));
                            try {
                                InputStream is = httpRequest.execute();
                                String result = Util.inputStream2String(is, "UTF-8");
                                httpRequest.finish();

                                Log.d("HTTP_REQUEST_RESULT", result);

                                JSONObject jsonObject = new JSONObject(result);
                                final int success = jsonObject.getInt("success");
                                if(success == 1) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            btnFavoritar.setTag(R.drawable.ic_favorito);
                                            btnFavoritar.setImageResource(R.drawable.ic_favorito);
                                        }
                                    });
                                }
                                else {
                                    final String message = jsonObject.getString("message");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(RecipeDisplayActivity.this, message, Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                if(resource == R.drawable.ic_favorito) {
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            HttpRequest httpRequest = new HttpRequest(Config.SERVER_URL_BASE + "favorita_desfavorita.php", "POST", "UTF-8");
                            httpRequest.setBasicAuth(email, senha);
                            httpRequest.addParam("username", username);
                            httpRequest.addParam("opcao", "0");
                            httpRequest.addParam("id_receita", String.valueOf(recipe.getId()));
                            try {
                                InputStream is = httpRequest.execute();
                                String result = Util.inputStream2String(is, "UTF-8");
                                httpRequest.finish();

                                JSONObject jsonObject = new JSONObject(result);
                                final int success = jsonObject.getInt("success");
                                if(success == 1) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            btnFavoritar.setTag(R.drawable.ic_favoritar);
                                            btnFavoritar.setImageResource(R.drawable.ic_favoritar);
                                        }
                                    });
                                }
                                else {
                                    final String message = jsonObject.getString("message");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(RecipeDisplayActivity.this, message, Toast.LENGTH_LONG).show();

                                        }
                                    });
                                }
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

        Button btnAlterarRendimento = findViewById(R.id.btnAlterarRendimento);
        btnAlterarRendimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText novoRendimento = findViewById(R.id.etNovoRendimento);
                if(!novoRendimento.getText().toString().isEmpty()) {
                    int y = Integer.parseInt(novoRendimento.getText().toString());

                    //percorrendo o array de lista de ingredientes
                    for(int i=0; i<ingredientes.getValue().size(); i++) {
                        int r = recipe.getRendimento();
                        float x = Float.parseFloat(ingredientes.getValue().get(i).getQuantidade());
                        String z = String.valueOf((y*x)/r);

                        ingredientes.getValue().get(i).setQuantidade(z);
                        ingredientes.observe(RecipeDisplayActivity.this, new Observer<List<Ingrediente>>() {
                            @Override
                            public void onChanged(List<Ingrediente> ingredientes) {
                                IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(RecipeDisplayActivity.this, ingredientes);
                                rvIngredientes.setAdapter(ingredientsAdapter);
                            }
                        });
                    }
                    recipe.setRendimento(y);
                }
            }
        });

    }
}