package com.foobnix.pdf.info;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;

import com.foobnix.android.utils.LOG;
import com.foobnix.android.utils.Objects;
import com.foobnix.android.utils.TxtUtils;
import com.foobnix.ext.CacheZipUtils;
import com.foobnix.model.AppBook;
import com.foobnix.model.AppProfile;
import com.foobnix.pdf.info.model.BookCSS;
import com.foobnix.pdf.info.wrapper.UITab;
import com.foobnix.ui2.BooksService;
import com.foobnix.ui2.MainTabs2;

import java.io.ByteArrayInputStream;
import java.io.File;

public class Clouds {

    private static final String TOKEN_EMPTY = "[{}]";
    public static final String LIBRERA_SYNC_ONLINE_FOLDER = "/Librera.Cloud";
    public static final String PREFIX_CLOUD = "cloud-";
    public static final String PREFIX_CLOUD_DROPBOX = PREFIX_CLOUD + "dropbox:";
    public static final String PREFIX_CLOUD_GDRIVE = PREFIX_CLOUD + "gdrive:";
    public static final String PREFIX_CLOUD_ONEDRIVE = PREFIX_CLOUD + "onedrive:";

    private static final Clouds instance = new Clouds();

    transient SharedPreferences sp;

    public volatile String dropboxToken;
    public volatile String googleDriveToken;
    public volatile String oneDriveToken;

    public volatile String dropboxInfo;
    public volatile String googleDriveInfo;
    public volatile String oneDriveInfo;

    public volatile String dropboxSpace;
    public volatile String googleSpace;
    public volatile String oneDriveSpace;
    private Context context;

    public static void saveProgress(AppBook bs) {
        LOG.d("Save progress", bs);
        if (!isCloudImage(bs.path)) {
            return;
        }
        File bookFile = new File(bs.path);
        File folder = new File(bookFile.getParentFile(), ".data");
        folder.mkdirs();

        File settings = new File(folder, bookFile.getName());
        try {
            CacheZipUtils.copyFile(new ByteArrayInputStream(Objects.toJSONString(bs).getBytes()), settings);
        } catch (Exception e) {
            LOG.e(e);
        }

    }

    public static boolean isCloudFile(String path) {
        return path.startsWith(Clouds.PREFIX_CLOUD) && path.lastIndexOf('.') > (path.length() - 6);
    }

    public static boolean isCloudDir(String path) {
        return path.startsWith(Clouds.PREFIX_CLOUD) && !isCloudFile(path);
    }

    public static boolean isCloud(String path) {
        return path.startsWith(PREFIX_CLOUD);
    }

    public static boolean isCloudImage(String path) {
        return path.contains("Librera.Cloud");
    }

    public static void runSync(Activity a) {
        BooksService.startForeground(a, BooksService.ACTION_SYNC_DROPBOX);
    }

    public static File getCacheFile(String path) {

        if (!path.startsWith(PREFIX_CLOUD)) {
            return null;
        }

        File cacheDir = new File(BookCSS.get().cachePath);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }

        String displayName = path.hashCode() + "_" + ExtUtils.getFileName(path);

        if (!path.contains(".Cloud/")) {
            File cacheFile = new File(BookCSS.get().cachePath, displayName);
            LOG.d("cacheFile-1", cacheFile);
            return cacheFile;
        }

        displayName = ExtUtils.getFileName(path);

        File cacheFile2 = null;
        if (path.startsWith(PREFIX_CLOUD_DROPBOX)) {
            cacheFile2 = new File(BookCSS.get().syncDropboxPath, displayName);
        } else if (path.startsWith(PREFIX_CLOUD_GDRIVE)) {
            cacheFile2 = new File(BookCSS.get().syncGdrivePath, displayName);
        } else if (path.startsWith(PREFIX_CLOUD_ONEDRIVE)) {
            cacheFile2 = new File(BookCSS.get().syncOneDrivePath, displayName);
        } else {
            cacheFile2 = new File(BookCSS.get().cachePath, displayName);
        }
        LOG.d("cacheFile-2", cacheFile2);

        return cacheFile2;

    }

    public static boolean isCacheFileExist(String path) {
        File cacheFile = getCacheFile(path);
        return cacheFile != null && cacheFile.isFile();

    }

    public static String getPath(String pathWithPrefix) {
        return pathWithPrefix.replace(PREFIX_CLOUD_DROPBOX, "").replace(PREFIX_CLOUD_GDRIVE, "").replace(PREFIX_CLOUD_ONEDRIVE, "");
    }

    public void logout(String path) {
        LOG.d("Logout", path);

        if (path.startsWith(PREFIX_CLOUD_DROPBOX)) {
            dropboxInfo = null;
            dropboxToken = null;
        }
        if (path.startsWith(PREFIX_CLOUD_GDRIVE)) {
            googleDriveInfo = null;
            googleDriveToken = null;
        }

        if (path.startsWith(PREFIX_CLOUD_ONEDRIVE)) {
            oneDriveInfo = null;
            oneDriveToken = null;
        }
        save();

    }

    public String getUserLogin(String path) {
        if (path.startsWith(PREFIX_CLOUD_DROPBOX)) {
            return dropboxInfo;
        }
        if (path.startsWith(PREFIX_CLOUD_GDRIVE)) {
            return googleDriveInfo;
        }
        if (path.startsWith(PREFIX_CLOUD_ONEDRIVE)) {
            return oneDriveInfo;
        }
        return "";
    }

    public static String getPrefix(String path) {
        if (path.startsWith(PREFIX_CLOUD_DROPBOX)) {
            return PREFIX_CLOUD_DROPBOX;
        }
        if (path.startsWith(PREFIX_CLOUD_GDRIVE)) {
            return PREFIX_CLOUD_GDRIVE;
        }
        if (path.startsWith(PREFIX_CLOUD_ONEDRIVE)) {
            return PREFIX_CLOUD_ONEDRIVE;
        }
        return "";
    }

    public static String getPrefixName(String path) {
        if (path.startsWith(PREFIX_CLOUD_DROPBOX)) {
            return "Dropbox";
        }
        if (path.startsWith(PREFIX_CLOUD_GDRIVE)) {
            return "GDrive";
        }
        if (path.startsWith(PREFIX_CLOUD_ONEDRIVE)) {
            return "OneDrive";
        }
        return "File";
    }

    public void init(Context c) {
        this.context = c;
    }

    public void syncronizeGet() {

    }


    public static boolean isSyncFileExist(File file) {
        File sync = new File(BookCSS.get().syncDropboxPath, file.getName());
        return sync.exists() && sync.length() > 0;
    }

    public static boolean isLibreraSyncFile(File path) {
        return isLibreraSyncFile(path.getPath());
    }

    public static boolean isLibreraSyncFile(String path) {
        if (TxtUtils.isEmpty(path)) {
            return false;
        }
        return path.startsWith(AppProfile.SYNC_FOLDER_BOOKS.getPath()) || new File(AppProfile.SYNC_FOLDER_BOOKS, ExtUtils.getFileName(path)).exists();
    }

    public static boolean isLibreraSyncRootFolder(String path) {
        if (TxtUtils.isEmpty(path)) {
            return false;
        }
        return path.startsWith(AppProfile.SYNC_FOLDER_ROOT.getPath());
    }

    public static boolean showHideCloudImage(ImageView img, String path) {

        String fileName = ExtUtils.getFileName(path);

        if (isLibreraSyncFile(path)) {
            img.setVisibility(View.VISIBLE);
            img.setImageResource(R.drawable.glyphicons_748_synchronization);
            //TintUtil.setTintImageNoAlpha(img, TintUtil.cloudSyncColor);

            TintUtil.setTintImageWithAlpha(img, img.getContext() instanceof MainTabs2 ? TintUtil.getColorInDayNighth() : TintUtil.getColorInDayNighthBook());

            return true;
        } else {
            img.setVisibility(View.GONE);
            img.setImageDrawable(null);
        }


        if (!AppsConfig.isCloudsEnable) {
            img.setVisibility(View.GONE);
            return false;
        }

        if (path.contains(Clouds.LIBRERA_SYNC_ONLINE_FOLDER)) {
            img.setVisibility(View.VISIBLE);
            if (path.contains(BookCSS.LIBRERA_CLOUD_DROPBOX) || path.startsWith(Clouds.PREFIX_CLOUD_DROPBOX)) {
                img.setImageResource(R.drawable.dropbox);
            } else if (path.contains(BookCSS.LIBRERA_CLOUD_GOOGLEDRIVE) || path.startsWith(Clouds.PREFIX_CLOUD_GDRIVE)) {
                img.setImageResource(R.drawable.gdrive);
            } else if (path.contains(BookCSS.LIBRERA_CLOUD_ONEDRIVE) || path.startsWith(Clouds.PREFIX_CLOUD_ONEDRIVE)) {
                img.setImageResource(R.drawable.onedrive);
            } else {
                // img.setImageResource(R.drawable.star_1);
                img.setVisibility(View.GONE);
            }
            return true;
        } else {
            img.setVisibility(View.GONE);
            return false;
        }
    }

    public void save() {
        LOG.d("CloudRail save");
        Objects.saveToSP(this, sp);

    }

    public static Clouds get() {
        return instance;
    }

}
