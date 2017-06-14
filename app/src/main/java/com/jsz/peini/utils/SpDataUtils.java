package com.jsz.peini.utils;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class SpDataUtils {
    public static final String PeiNiFileCache = "PEINI_FILE_CACHE";

    public static void put(Context context, String setFileName, String squareText) {
        if (TextUtils.isEmpty(squareText)) {
            return;
        }
        File filesDir = context.getFilesDir();
        File square = new File(filesDir, PeiNiFileCache);
        if (!square.isFile() && !square.exists()) {
            square.mkdirs();
        } else {
            square.delete();
        }
        Writer writer = null;
        try {
            File file = new File(square, setFileName + SpUtils.getPhone(context));
            FileOutputStream out = new FileOutputStream(file);
            writer = new OutputStreamWriter(out);
            writer.write(squareText);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String get(Context context, String getFile, String NoFile) {
        BufferedReader reader = null;
        StringBuilder data = new StringBuilder();
        try {
            File sdCard = context.getFilesDir();
            sdCard = new File(sdCard, "/" + PeiNiFileCache + "/" + getFile + SpUtils.getPhone(context));
            if (!sdCard.exists()) {
                return NoFile;
            }
            FileInputStream in = new FileInputStream(sdCard);
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null)
                data.append(line);
            return data.toString();
        } catch (Exception e) {
            return NoFile;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean setDelete(Context context, String getFile, boolean deletefile) {
        try {
            File sdCard = context.getFilesDir();
            sdCard = new File(sdCard, "/" + PeiNiFileCache + "/" + getFile + SpUtils.getPhone(context));
            if (!sdCard.exists()) {
                return sdCard.delete();
            }
        } catch (Exception e) {
            return deletefile;
        }
        return deletefile;
    }
}
