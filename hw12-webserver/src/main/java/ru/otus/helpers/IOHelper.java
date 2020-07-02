package ru.otus.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IOHelper {
    public static String readAsString(InputStream in) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(in))) {
            String inputLine;
            while ((inputLine = r.readLine()) != null) {
                response.append(inputLine);
            }
        }
        return response.toString();
    }
}
