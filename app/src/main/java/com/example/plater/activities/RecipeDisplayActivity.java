package com.example.plater.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
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
import com.example.plater.utils.ImageCache;
import com.example.plater.utils.Util;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecipeDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //  obtendo qual foi a receita selecionada
        Intent i = getIntent();
        Recipe recipe = (Recipe) i.getSerializableExtra("recipe");

        final String email = Config.getLogin(RecipeDisplayActivity.this);
        final String senha = Config.getPassword(RecipeDisplayActivity.this);
        final String username = Config.getUsername(RecipeDisplayActivity.this);

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        if(recipe.getUrlType().equals("t")) {
            setContentView(R.layout.activity_recipe_display);

            ImageView imvRecipePhoto = findViewById(R.id.imvRecipePhoto);
            ImageCache.loadToImageView(RecipeDisplayActivity.this, String.valueOf(recipe.getId()), imvRecipePhoto, recipe.getMediaUrl());
        }
        else {
            setContentView(R.layout.activity_recipe_display_video);

            YouTubePlayer.OnInitializedListener onInitializedListener = new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                    youTubePlayer.loadVideo(recipe.getMediaUrl());
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                }
            };

            YouTubePlayerFragment youTubePlayerFragment = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.fgRecipeVideo);
            youTubePlayerFragment.initialize(Config.API_KEY, onInitializedListener);
        }

        ImageView btnFavoritar = findViewById(R.id.btnFavoritar);
        btnFavoritar.setTag(R.drawable.ic_favoritar);

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
                    int r = recipe.getRendimento();                                 // rendimento real da receita
                    int y = Integer.parseInt(novoRendimento.getText().toString());  // novo rendimento da receita

                    //percorrendo o array de lista de ingredientes
                    for(int i=0; i<ingredientes.getValue().size(); i++) {
                        float x = fracao2decimal(ingredientes.getValue().get(i).getQuantidade());   // ex.: 1/2 para 0.5
                        float z = (y*x)/r;                                                          // obtem o novo rendimento em decimal
                        String numeroFinal = decimal2fracao(z);                                     // ex.: 1.5 para 1 1/2

                        ingredientes.getValue().get(i).setQuantidade(numeroFinal);
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

    private float fracao2decimal(String entrada) {
        float a;    // o primeiro numero da fracao do rendimento
        float b;    // o segundo numero da fracao do rendimento
        float c = 0; // o inteiro do rendimento
        float rendimento;
        float d = 0;

        //significa que tem inteiro e fracionado
        if(entrada.length() > 1) {
            if(entrada.length() == 5) {
                a = Float.parseFloat(entrada.substring(2, 3));
                b = Float.parseFloat(entrada.substring(4));
                c = Float.parseFloat(entrada.substring(0,1));
            }
            else {
                a = Float.parseFloat(entrada.substring(0, 1));
                b = Float.parseFloat(entrada.substring(2));
            }
            d = (a/b); //parte decimal
            rendimento = d + c; //rendimento inteiro da receita
        }
        else {
            rendimento = Float.parseFloat(entrada); //rendimento inteiro da receita
        }

        return rendimento;
    }

    private String decimal2fracao(float rendimento) {
        int i = (int) rendimento;
        float d = rendimento - i;

        String numeroFinal = "";
        String fracao = "";

        //se tiver numero decimal
        if(d > 0 && d <= 0.83 ) {
            if(d < 0.12) { d = 0; }
            if((0.12<d) && (d<=0.295)) { fracao = "1/4"; }
            if((0.295<d) && (d<=0.4)) { fracao = "1/3"; }
            if((0.4<d) && (d<=0.582)) { fracao = "1/2"; }
            if((0.582<d) && (d<=0.7)) { fracao = "2/3"; }
            if((0.7<d) && (d<=0.83)) { fracao = "3/4"; }

            if(i == 0) {
                numeroFinal = fracao;
            }
            else {
                numeroFinal = String.valueOf(i) + " " + fracao;
            }
        }
        if(d > 0.83) {
            numeroFinal = String.valueOf(i + 1);
        }
        if(d == 0) {
            numeroFinal = String.valueOf(i);
        }

        return numeroFinal;
    }

}