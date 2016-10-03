package com.example.raphael.tcc;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by rapha on 17-Sep-16.
 */
public class ReadWriteFile{
    public static String read(String path) throws IOException {
        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        output.append(reader.readLine());
        for (String line = reader.readLine(); line != null; line = reader.readLine())
            output.append('\n').append(line);
        reader.close();
        return output.toString();
    }
    public static void createFile(Context context){
        String fileName = "MyFile";
        String content = "O botao foi apertado e eu fui chamado!";
        try {
            FileOutputStream fOut = context.openFileOutput(fileName,context.MODE_PRIVATE);
            fOut.write(content.getBytes());
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
