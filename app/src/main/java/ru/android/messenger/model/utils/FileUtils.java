package ru.android.messenger.model.utils;

import android.content.Context;
import android.content.ContextWrapper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import okhttp3.ResponseBody;
import ru.android.messenger.utils.Logger;

public class FileUtils {

    private static final String IMAGES_DIR_NAME = "images";
    private static final String PROFILE_IMAGE_NAME = "profile.jpg";

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

    public static File getFileFromResponseBody(ResponseBody responseBody, Context context) {
        File file = new File(context.getExternalFilesDir(null) +
                File.separator + UUID.randomUUID());

        try (InputStream inputStream = responseBody.byteStream();
             OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            byte[] buffer = new byte[4096];
            while (true) {
                int read = inputStream.read(buffer);
                if (read == -1) {
                    break;
                }
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();
        } catch (IOException e) {
            Logger.error("Error with read file from server", e);
            return null;
        }
        return file;
    }

    public static void saveUserPhotoToInternalStorage(File photo, Context context) {
        ContextWrapper contextWrapper = new ContextWrapper(context);
        String destinationFileName =
                contextWrapper.getDir(IMAGES_DIR_NAME, Context.MODE_PRIVATE) + PROFILE_IMAGE_NAME;
        copyFile(photo, destinationFileName);
        deleteFile(photo);
    }

    public static String getPathToPhoto(Context context) {
        ContextWrapper contextWrapper = new ContextWrapper(context);
        return contextWrapper.getDir(IMAGES_DIR_NAME, Context.MODE_PRIVATE) + PROFILE_IMAGE_NAME;
    }

    private static void copyFile(File file, String destination) {
        File newFile = new File(destination);
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
             OutputStream out = new BufferedOutputStream(new FileOutputStream(newFile))) {
            byte[] buffer = new byte[1024];
            int lengthRead;
            while ((lengthRead = inputStream.read(buffer)) > 0) {
                out.write(buffer, 0, lengthRead);
                out.flush();
            }
        } catch (IOException e) {
            Logger.error("Error with copy file", e);
        }
    }

    @SuppressWarnings("squid:S4042")
    private static void deleteFile(File file) {
        if (file.delete()) {
            String warningMessage = String.format("File %s not deleted", file.getPath());
            Logger.warning(warningMessage);
        }
    }
}
