package br.com.tocalivros.mupdfsample.application;

import android.app.Application;
import android.content.Context;

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

public class MuPDFApplication extends Application {

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
