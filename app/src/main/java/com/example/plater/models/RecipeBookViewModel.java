package com.example.plater.models;

import static java.lang.Integer.parseInt;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
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

public class RecipeBookViewModel extends AndroidViewModel {

    MutableLiveData<List<Recipe>> favoriteRecipesList;
    String username;
    String senha;
    MyDB db;

    public RecipeBookViewModel(@NonNull Application application) {
        super(application);
        db = MyDB.getDatabase(application);

        this.username = Config.getLogin(application);
        this.senha = Config.getPassword(application);
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
                            int id = parseInt(jRecipe.getString("id_receita"));

                            Recipe recipe = db.myDao().getRecipe(id);
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

}
