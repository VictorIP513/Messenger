package ru.android.messenger.model.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class ImageHelper {

    private static final Bitmap.CompressFormat COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG;
    private static final int QUALITY = 100;

    private static final String FILE_PREFIX = "image";
    private static final String FILE_SUFFIX = ".jpg";

    private ImageHelper() {

    }

    public static File writeBitmapToFile(Bitmap bitmap, Context context) throws IOException {
        File file = File.createTempFile(FILE_PREFIX, FILE_SUFFIX, context.getCacheDir());
        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            bitmap.compress(COMPRESS_FORMAT, QUALITY, outputStream);
        }
        return file;
    }

    public static Bitmap getBitmapFromUriAndContext(Uri imageUri, Context context)
            throws FileNotFoundException {
        InputStream imageStream = context.getContentResolver().openInputStream(
                Objects.requireNonNull(imageUri));
        return BitmapFactory.decodeStream(imageStream);
    }

    public static byte[] getBitmapFromByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(COMPRESS_FORMAT, QUALITY, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
