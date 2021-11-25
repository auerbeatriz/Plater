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

import com.example.plater.Ingrediente;
import com.example.plater.PassoPreparo;
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

public class RecipeDisplayViewModel extends AndroidViewModel {

    int idReceita;
    MyDB db;
    MutableLiveData<List<Ingrediente>> ingredientes;
    MutableLiveData<List<PassoPreparo>> modoPreparo;

    public RecipeDisplayViewModel(@NonNull Application application, int idReceita) {
        super(application);

        db = MyDB.getDatabase(application);
        this.idReceita = idReceita;
    }

    public LiveData<List<Ingrediente>> getIngredients() {
        ingredientes = new MutableLiveData<List<Ingrediente>>();
        loadIngredients();
        return ingredientes;
    }

    public LiveData<List<PassoPreparo>> getModoPreparo() {
        modoPreparo = new MutableLiveData<List<PassoPreparo>>();
        loadModoPreparo();
        return modoPreparo;
    }

    private void loadIngredients() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                /* verificamos se existem ingredientes para essa receita no banco de dados local
                 * se houver, então passamos esses dados pro mutablelivedata e partiu pro abraço
                 * senão, entramos em contato com o servidor para obter
                 */
                List<Ingrediente> ingredientsList = db.myDao().getRecipeIngredients(idReceita);
                if(ingredientsList.size() > 0) {
                    ingredientes.postValue(ingredientsList);
                }
                else {
                    HttpRequest httpRequest = new HttpRequest(Config.SERVER_URL_BASE + "carrega_ingredientes.php", "GET", "UTF-8");
                    httpRequest.addParam("id_receita", String.valueOf(idReceita));
                    try {
                        InputStream is = httpRequest.execute();
                        String result = Util.inputStream2String(is, "UTF-8");
                        httpRequest.finish();

                        Log.d("HTTP_REQUEST_RESULT", result);

                        JSONObject jsonObject = new JSONObject(result);
                        int success = jsonObject.getInt("success");
                        if (success == 1) {
                            JSONArray jsonArray = jsonObject.getJSONArray("ingredientes");

                            for(int i=0; i<jsonArray.length(); i++) {
                                JSONObject jIngredient = jsonArray.getJSONObject(i);

                                int id = parseInt(jIngredient.getString("id"));
                                String quantidade = jIngredient.getString("quantidade");
                                String unidadeMedida = jIngredient.getString("unidade_medida");
                                String insumo = jIngredient.getString("insumo");

                                Ingrediente ingrediente = new Ingrediente(id, quantidade, unidadeMedida, insumo, idReceita);
                                db.myDao().insertIngrediente(ingrediente);
                                ingredientsList.add(ingrediente);
                            }

                            ingredientes.postValue(ingredientsList);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void loadModoPreparo() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<PassoPreparo> tutorial = db.myDao().getRecipeInstructions(idReceita);
                if(tutorial.size() > 0) {
                    modoPreparo.postValue(tutorial);
                }
                else {
                    HttpRequest httpRequest = new HttpRequest(Config.SERVER_URL_BASE + "carrega_passo_preparo.php", "GET", "UTF-8");
                    httpRequest.addParam("id_receita", String.valueOf(idReceita));
                    try {
                        InputStream is = httpRequest.execute();
                        String result = Util.inputStream2String(is, "UTF-8");
                        httpRequest.finish();

                        Log.d("HTTP_REQUEST_RESULT", result);

                        JSONObject jsonObject = new JSONObject(result);
                        int success = jsonObject.getInt("success");
                        if (success == 1) {
                            JSONArray jsonArray = jsonObject.getJSONArray("modo_preparo");

                            for(int i=0; i<jsonArray.length(); i++) {
                                JSONObject jPassoPreparo = jsonArray.getJSONObject(i);

                                int id = parseInt(jPassoPreparo.getString("id"));
                                String instrucao = jPassoPreparo.getString("instrucao");

                                PassoPreparo passoPreparo = new PassoPreparo(id, instrucao, idReceita);
                                db.myDao().insertPassoPreparo(passoPreparo);
                                tutorial.add(passoPreparo);
                            }

                            modoPreparo.postValue(tutorial);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    //  Como o ViewModelProvider é incapaz de construir classes que aceitam parâmetros, temos que ensinar ele a fazer isso
    static public class RecipeDisplayViewModelFactory implements ViewModelProvider.Factory {

        private Application application;
        private int idReceita;

        public RecipeDisplayViewModelFactory(Application application, int idReceita) {
            this.application = application;
            this.idReceita = idReceita;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new RecipeDisplayViewModel(application, idReceita);
        }
    }
}
