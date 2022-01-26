package com.example.plater.models;

import static java.lang.Integer.parseInt;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import com.example.plater.R;
import com.example.plater.Recipe;
import com.example.plater.utils.Config;
import com.example.plater.utils.HttpRequest;
import com.example.plater.utils.MyDB;
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

public class MainActivityViewModel extends AndroidViewModel {

    int bottomViewNavigationOp = R.id.homeView;
    MutableLiveData<List<Recipe>> recipes;
    MyDB db;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        db = MyDB.getDatabase(application);
    }

    public LiveData<List<Recipe>> getRecipeList() {
        //  se ainda nao existirem receitas na lista, conecta-se ao servidor para obte-las;
        if(recipes == null) {
            recipes = new MutableLiveData<List<Recipe>>();
            loadRecipes();
        }
        return recipes;
    }

    void loadRecipes() {
        final String email = Config.getLogin(getApplication());
        final String senha = Config.getPassword(getApplication());

        //  criando uma nova thread para obter os dados
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                //  lista de receitas local
                List<Recipe> recipesList = db.myDao().getAllRecipes();
                int lastRecipe;
                if(recipesList.size() > 0) {
                    recipes.postValue(recipesList);
                    lastRecipe = recipesList.get(recipesList.size() - 1).getId();
                }
                else { lastRecipe = 0; }

                HttpRequest httpRequest = new HttpRequest(Config.SERVER_URL_BASE + "carrega_todas_receitas.php", "GET", "UTF-8");
                httpRequest.setBasicAuth(email, senha);
                httpRequest.addParam("lastRecipe", String.valueOf(lastRecipe));
                try {
                    InputStream is = httpRequest.execute();
                    String result = Util.inputStream2String(is, "UTF-8");
                    httpRequest.finish();

                    JSONObject jsonObject = new JSONObject(result);
                    int success = jsonObject.getInt("success");
                    if (success == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("recipes");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jRecipe = jsonArray.getJSONObject(i);

                            //TODO: AJUSTAR ESSA LISTA PARA RECEBER A MILTIMIDIA
                            int id = parseInt(jRecipe.getString("id_receita"));
                            String titulo = jRecipe.getString("titulo_receita");
                            String descricao = jRecipe.getString("descricao_receita");
                            String tempoPreparo = jRecipe.getString("tempo_preparo");
                            int rendimento = parseInt(jRecipe.getString("rendimento"));
                            String tipoRendimento = jRecipe.getString("tipo_rendimento");
                            int idCategoria = parseInt(jRecipe.getString("id_categoria"));
                            String categoria = jRecipe.getString("categoria");
                            String url = jRecipe.getString("url");
                            String urlType = jRecipe.getString("urlType");

                            Recipe recipe = new Recipe(id, titulo, descricao, tempoPreparo, rendimento, tipoRendimento, idCategoria, categoria, url, urlType);
                            db.myDao().insertRecipe(recipe);
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
