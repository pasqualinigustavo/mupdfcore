package org.ebookdroid;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Environment;

import br.com.tocalivros.mupdfsample.foobnix.android.utils.Dips;
import br.com.tocalivros.mupdfsample.foobnix.android.utils.LOG;
import br.com.tocalivros.mupdfsample.foobnix.ext.CacheZipUtils;
import br.com.tocalivros.mupdfsample.foobnix.pdf.info.AppSharedPreferences;
import br.com.tocalivros.mupdfsample.foobnix.pdf.info.ExtUtils;
import br.com.tocalivros.mupdfsample.foobnix.pdf.info.IMG;
import br.com.tocalivros.mupdfsample.foobnix.pdf.info.TintUtil;
import br.com.tocalivros.mupdfsample.foobnix.pdf.info.wrapper.AppState;
import br.com.tocalivros.mupdfsample.foobnix.ui2.AppDB;

import org.ebookdroid.common.bitmaps.BitmapManager;
import org.ebookdroid.common.cache.CacheManager;
import org.ebookdroid.common.settings.SettingsManager;

public class LibreraApp extends Application {

    static {
        System.loadLibrary("mypdf");
        System.loadLibrary("mobi");
    }

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        Dips.init(context);
        AppDB.get().open(this);
        AppState.get().load(this);
        AppSharedPreferences.get().init(this);
        CacheZipUtils.init(context);
        ExtUtils.init(getApplicationContext());
        IMG.init(getApplicationContext());

        TintUtil.init();

        SettingsManager.init(this);
        CacheManager.init(this);

        LOG.d("Build", "Build.MANUFACTURER", Build.MANUFACTURER);
        LOG.d("Build", "Build.PRODUCT", Build.PRODUCT);
        LOG.d("Build", "Build.DEVICE", Build.DEVICE);
        LOG.d("Build", "Build.BRAND", Build.BRAND);
        LOG.d("Build", "Build.MODEL", Build.MODEL);

        LOG.d("Build.Context", "Context.getFilesDir()", getFilesDir());
        LOG.d("Build.Context", "Context.getCacheDir()", getCacheDir());
        LOG.d("Build.Context", "Context.getExternalCacheDir", getExternalCacheDir());
        LOG.d("Build.Context", "Context.getExternalFilesDir(null)", getExternalFilesDir(null));
        LOG.d("Build.Context", "Environment.getExternalStorageDirectory()", Environment.getExternalStorageDirectory());
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        LOG.d("AppState save onLowMemory");
        IMG.clearMemoryCache();
        BitmapManager.clear("on Low Memory: ");
        TintUtil.clean();
    }

}
