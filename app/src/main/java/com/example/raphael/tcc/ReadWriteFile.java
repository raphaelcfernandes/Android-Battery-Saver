package com.example.raphael.tcc;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by rapha on 17-Sep-16.
 */
public class ReadWriteFile{
    File file;
    StringBuilder teste3 = new StringBuilder();
    FileOutputStream outputStream;
    public static String read(String path) throws IOException {
        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        output.append(reader.readLine());
        for (String line = reader.readLine(); line != null; line = reader.readLine())
            output.append('\n').append(line);
        reader.close();
        return output.toString();
    }
    public void createFile(Context context,String arg){
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
