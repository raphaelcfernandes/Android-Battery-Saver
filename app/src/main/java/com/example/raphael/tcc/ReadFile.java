package com.example.raphael.tcc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by rapha on 17-Sep-16.
 */
public class ReadFile {
    public static String read(String path) throws IOException {
        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        output.append(reader.readLine());
        for (String line = reader.readLine(); line != null; line = reader.readLine())
            output.append('\n').append(line);
        reader.close();
        return output.toString();
    }
}
