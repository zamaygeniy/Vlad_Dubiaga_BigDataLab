package com.epam.crimes.service.impl;

import com.epam.crimes.service.Reader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReaderImpl implements Reader {

    public static final String DOUBLE_REGEX = "-?\\d+\\.?\\d+";

    @Override
    public List<Double> readFromFile(String filename) {
        Path path = Paths.get(filename);
        List<Double> list = new ArrayList<>();
        try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line = reader.readLine();
            Pattern pattern = Pattern.compile(DOUBLE_REGEX);
            while (line != null) {
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    list.add(Double.parseDouble(matcher.group()));
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return list;
    }
}
