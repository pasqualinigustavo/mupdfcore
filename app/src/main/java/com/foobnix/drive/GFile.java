package com.foobnix.drive;

import android.content.SharedPreferences;

import com.foobnix.android.utils.LOG;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class GFile {
    public static final int REQUEST_CODE_SIGN_IN = 1110;


    public static final String MIME_FOLDER = "application/vnd.google-apps.folder";

    public static final String TAG = "GFile";
    public static final int PAGE_SIZE = 1000;
    public static final String SKIP = "skip";
    public static final String LASTMODIFIED = "lastmodified2";
    public static String debugOut = new String();

    static SharedPreferences sp;


    public static void setLastModifiedTime(java.io.File file, long lastModified) {
        if (file.isFile()) {
            for (String key : sp.getAll().keySet()) {
                if (key.startsWith(file.getPath())) {
                    sp.edit().remove(key).commit();
                    LOG.d("hasLastModified remove", key);
                }
            }
        }
        sp.edit().putLong(file.getPath() + file.lastModified(), lastModified).commit();
        LOG.d("hasLastModified put", file.getPath() + file.lastModified(), lastModified);

    }

    public static boolean hasLastModified(java.io.File file) {
        for (String key : sp.getAll().keySet()) {
            if (key.startsWith(file.getPath())) {
                return true;
            }
        }
        return false;
    }

    public static long getLastModified(java.io.File file) {
        if (file.lastModified() == 0) {
            return 0;
        }
        return sp.getLong(file.getPath() + file.lastModified(), file.lastModified());
    }

    public static volatile boolean isNeedUpdate = false;

    static Map<java.io.File, File> map2 = new HashMap<>();

    public static long timeout = 0;

}

