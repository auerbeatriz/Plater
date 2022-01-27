package com.example.plater.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

    /**
     * Carrega um arquivo de imagem em um bitmap sem realizar qualquer tipo de escala.
     * @param imagePath caminho local de onde esta localizado o arquivo de imagem.
     * @return o bitmap.
     */
    public static Bitmap getBitmap(String imagePath) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imagePath, bmOptions);
    }

    /**
     * Salva um Bitmap em um arquivo.
     * @param bmp bitmap a ser salvo
     * @param imageLocation caminho absoluto onde esta salva a imagem
     * @throws FileNotFoundException se o arquivo nao existe
     */
    public static void saveImage(Bitmap bmp, String imageLocation) throws FileNotFoundException {
        FileOutputStream out = new FileOutputStream(imageLocation);
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
    }

    public static Bitmap resizeBitmap(Bitmap source, int maxLength) {
        try {
            if (source.getHeight() >= source.getWidth()) {
                int targetHeight = maxLength;
                if (source.getHeight() <= targetHeight) { // if image already smaller than the required height
                    return source;
                }

                double aspectRatio = (double) source.getWidth() / (double) source.getHeight();
                int targetWidth = (int) (targetHeight * aspectRatio);

                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                if (result != source) {
                }
                return result;
            } else {
                int targetWidth = maxLength;

                if (source.getWidth() <= targetWidth) { // if image already smaller than the required height
                    return source;
                }

                double aspectRatio = ((double) source.getHeight()) / ((double) source.getWidth());
                int targetHeight = (int) (targetWidth * aspectRatio);

                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                if (result != source) {
                }
                return result;

            }
        }
        catch (Exception e)
        {
            return source;
        }
    }
}
