package br.com.tocalivros.foobnix.android.utils;

import android.graphics.Bitmap;
import android.view.View;

import br.com.tocalivros.foobnix.pdf.info.IMG;
import br.com.tocalivros.universalimageloader.core.ImageLoader;
import br.com.tocalivros.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Random;

public class Safe {

    public static final String TXT_SAFE_RUN = "SAFE_RUN-";

    static Random r = new Random();

    public static void run(final Runnable action) {
        ImageLoader.getInstance().clearAllTasks();

        ImageLoader.getInstance().loadImage(TXT_SAFE_RUN, IMG.noneOptions, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                LOG.d(TXT_SAFE_RUN, "end", imageUri);
                if (action != null) {
                    ImageLoader.getInstance().clearAllTasks();
                    action.run();
                }
            }
        });

    }
}
