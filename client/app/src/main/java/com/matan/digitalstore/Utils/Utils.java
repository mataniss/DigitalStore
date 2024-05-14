package com.matan.digitalstore.Utils;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Utils {
    public static File uriToFile(Context context, Uri uri) {
        File file = new File(context.getCacheDir(), "temp_image");
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
             OutputStream outputStream = new FileOutputStream(file)) {
            if (inputStream != null) {
                byte[] buffer = new byte[4 * 1024]; // buffer size
                int read;
                while ((read = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, read);
                }
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }
}
