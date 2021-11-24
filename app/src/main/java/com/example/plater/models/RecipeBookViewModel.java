package com.example.plater.models;

import static java.lang.Integer.parseInt;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.plater.R;
import com.example.plater.Recipe;
import com.example.plater.activities.MainActivity;
import com.example.plater.fragments.RecipeBookFragment;
import com.example.plater.utils.Config;
import com.example.plater.utils.HttpRequest;
import com.example.plater.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecipeBookViewModel extends ViewModel {

    MutableLiveData<List<Recipe>> favoriteRecipesList;
    String username;
    String senha;

    public RecipeBookViewModel(String username, String senha) {
        this.username = username;
        this.senha = senha;
    }

    public LiveData<List<Recipe>> getFavoriteRecipes() {
        //se ainda não existirem receitas na lista
        if(favoriteRecipesList == null) {
            this.favoriteRecipesList = new MutableLiveData<>();
            loadFavoriteRecipes();
        }
        return favoriteRecipesList;
    }

    private void loadFavoriteRecipes() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<Recipe> favoriteRecipes = new ArrayList<>();
                HttpRequest httpRequest = new HttpRequest(Config.SERVER_URL_BASE + "carrega_favoritas.php", "POST", "UTF-8");
                httpRequest.setBasicAuth(username, senha);
                try {
                    InputStream is = httpRequest.execute();
                    String result = Util.inputStream2String(is, "UTF-8");
                    httpRequest.finish();

                    Log.d("HTTP_REQUEST_RESULT", result);

                    JSONObject jsonObject = new JSONObject(result);
                    int success = jsonObject.getInt("success");
                    if (success == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("recipes");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jRecipe = jsonArray.getJSONObject(i);

                            //TODO: AJUSTAR ESSA LISTA PARA RECEBER A MILTIMIDIA
                            String id = jRecipe.getString("id_receita");
                            String titulo = jRecipe.getString("titulo_receita");
                            String descricao = jRecipe.getString("descricao_receita");
                            String tempoPreparo = jRecipe.getString("tempo_preparo");
                            int rendimento = parseInt(jRecipe.getString("rendimento"));
                            String tipoRendimento = jRecipe.getString("tipo_rendimento");
                            String categoria = jRecipe.getString("categoria");

                            Recipe recipe = new Recipe(id, titulo, descricao, tempoPreparo, rendimento, tipoRendimento, categoria);
                            favoriteRecipes.add(recipe);
                        }
                        //  avisa a products que a lista de produtos mudou
                        favoriteRecipesList.postValue(favoriteRecipes);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void refreshFavoriteRecipes() {
        loadFavoriteRecipes();
    }

    //  Como o ViewModelProvider é incapaz de construir classes que aceitam parâmetros, temos que ensinar ele a fazer isso
    static public class RecipeBookViewModelFactory implements ViewModelProvider.Factory {
        String username;
        String senha;

        public RecipeBookViewModelFactory(String username, String senha) {
            this.username = username;
            this.senha = senha;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new RecipeBookViewModel(username, senha);
        }
    }
}
