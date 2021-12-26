package com.epam.crimes.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileReader {

    public static final String DOUBLE_REGEX = "-?\\d+\\.?\\d+";

    public Queue<List<Double>> readCoordinatesFromFile(Path path) throws IOException {
        ConcurrentLinkedQueue<List<Double>> queue = new ConcurrentLinkedQueue<>();
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line = reader.readLine();
            Pattern pattern = Pattern.compile(DOUBLE_REGEX);
            while (line != null) {
                List<Double> list = new ArrayList<>();
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    list.add(Double.parseDouble(matcher.group()));
                }
                if (!list.isEmpty()){
                    queue.add(list);
                }
                line = reader.readLine();
            }
        }
        return queue;
    }
}
