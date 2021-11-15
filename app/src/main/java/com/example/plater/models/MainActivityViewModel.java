package com.example.plater.models;

import static java.lang.Integer.parseInt;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.plater.R;
import com.example.plater.Recipe;
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

public class MainActivityViewModel extends ViewModel {

    int bottomViewNavigationOp = R.id.homeView;
    MutableLiveData<List<Recipe>> recipes;

    public LiveData<List<Recipe>> getRecipeList() {
        //  se ainda nao existirem receitas na lista, conecta-se ao servidor para obte-las;
        if(recipes == null) {
            recipes = new MutableLiveData<List<Recipe>>();
            loadRecipes();
        }
        return recipes;
    }

    void loadRecipes() {
        //  criando uma nova thread para obter os dados
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<Recipe> recipesList = new ArrayList<>();
                HttpRequest httpRequest = new HttpRequest(Config.SERVER_URL_BASE + "carrega_todas_receitas.php", "GET", "UTF-8");
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
                            recipesList.add(recipe);
                        }
                        //  avisa a products que a lista de produtos mudou
                        recipes.postValue(recipesList);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void refreshRecipes() {
        loadRecipes();
    }

    public int getBottomViewNavigationOp() {
        return bottomViewNavigationOp;
    }

    //  guarda a opcao de navegacao do bottom_navigation_menu
    public void setBottomViewNavigationOp(int bottomViewNavigationOp) {
        this.bottomViewNavigationOp = bottomViewNavigationOp;
    }
}
