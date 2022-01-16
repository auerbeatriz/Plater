package com.example.plater.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Util {

    /**
     * Calcula o numero de colunas que cabem na tela ao usar o tipo de
     * visualizacao GRID no RecycleView
     *
     * @param context contexto utilizado, geralmente instancia da activity que
     *                contem o RecycleView
     * @param columnWidthDp largura do item do grid em dp
     * @return numero de colunas a serem usadas no GridLayoutManager
     */
    public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthdp=180
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels;
        int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
        return noOfColumns;
    }

    // Converte um IS para uma string
    public static String inputStream2String(InputStream is, String charset) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                is, charset), 8);
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        is.close();
        return sb.toString();
    }
}
