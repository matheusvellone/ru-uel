package com.hofideas.cardapioruuel.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileHandler {
    public static int VERSAO_ANDROID = Build.VERSION.SDK_INT;

    public static void writeToFile(String nomeArquivo, String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(nomeArquivo, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static String readFromFile(String nomeArquivo, Context context) throws IOException {
        String ret = "";
        InputStream inputStream = context.openFileInput(nomeArquivo);

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((receiveString = bufferedReader.readLine()) != null) {
                stringBuilder.append(receiveString);
            }

            inputStream.close();
            ret = stringBuilder.toString();
        }

        return ret;
    }
}