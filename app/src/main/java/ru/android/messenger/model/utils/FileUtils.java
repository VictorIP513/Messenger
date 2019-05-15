package ru.android.messenger.model.utils;

import android.content.Context;

import java.io.File;

import ru.android.messenger.utils.Logger;

public class FileUtils {

    private FileUtils() {

    }

    public static void deleteCache(Context context) {
        File dir = context.getCacheDir();
        boolean result = deleteDir(dir);
        if (!result) {
            Logger.warning("Cache files not deleted");
        }
    }

    @SuppressWarnings("squid:S4042")
    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}
