package com.example.plater.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageCache {

    /* Verifica se a imagem já foi salva dentro do diretório padrão Pictures
     * Se sim, carrega essa imagem no imageView passado.
     * Se não, salva a imagem no diretório padrão e então carrega no imageView passado.
     */
    public static void loadToImageView(Activity activity, String pid, ImageView imageView, String url) {
        String imageLocation = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + pid;
        File f = new File(imageLocation);

        if(f.exists() && !f.isDirectory()) {
            imageView.setImageBitmap(Util.getBitmap(imageLocation));
        }
        else {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    HttpRequest httpRequest = new HttpRequest(url, "GET", "UTF-8");
                    try {
                        InputStream is = httpRequest.execute();
                        Bitmap img = BitmapFactory.decodeStream(is);
                        is.close();
                        httpRequest.finish();

                        Util.saveImage(img, imageLocation);

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(img);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }
}
