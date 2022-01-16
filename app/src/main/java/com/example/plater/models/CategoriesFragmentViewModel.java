package com.example.plater.models;

import static java.lang.Integer.parseInt;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.plater.Category;
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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoriesFragmentViewModel extends AndroidViewModel {

    MutableLiveData<List<Category>> categories;
    MyDB db;

    public CategoriesFragmentViewModel(@NonNull Application application) {
        super(application);
        db = MyDB.getDatabase(application);
    }

    public LiveData<List<Category>> getCategoriesList() {
        if (categories == null) {
            categories = new MutableLiveData<List<Category>>();
            loadCategories();
        }
        return categories;
    }

    private void loadCategories() {

        final String email = Config.getLogin(getApplication());
        final String senha = Config.getPassword(getApplication());

        //  criando uma nova thread para obter os dados
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<Category> categoryList = db.myDao().getAllCategories();
                int lastCategory;
                if(categoryList.size() > 0) {
                    categories.postValue(categoryList);
                    lastCategory = categoryList.get(categoryList.size() - 1).getId();
                }
                else { lastCategory = 0; }

                HttpRequest httpRequest = new HttpRequest(Config.SERVER_URL_BASE + "carrega_categorias.php", "GET", "UTF-8");
                httpRequest.setBasicAuth(email, senha);
                httpRequest.addParam("lastCategory", String.valueOf(lastCategory));
                try {
                    InputStream is = httpRequest.execute();
                    String result = Util.inputStream2String(is, "UTF-8");
                    httpRequest.finish();

                    Log.d("HTTP_REQUEST_RESULT", result);

                    JSONObject jsonObject = new JSONObject(result);
                    int success = jsonObject.getInt("success");
                    if (success == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("categories");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jCategory = jsonArray.getJSONObject(i);

                            //TODO: AJUSTAR ESSA LISTA PARA RECEBER A MILTIMIDIA
                            int id = parseInt(jCategory.getString("id_categoria"));
                            String nome = jCategory.getString("categoria");
                            String url = jCategory.getString("img_categoria");

                            Category category = new Category(id, nome, url);
                            db.myDao().insertCategory(category);
                            categoryList.add(category);
                        }
                        //  avisa a products que a lista de produtos mudou
                        categories.postValue(categoryList);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void refreshCategories() { loadCategories(); }
}
