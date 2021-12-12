package com.epam.crimes.main;

import com.epam.crimes.entity.Crime;
import com.epam.crimes.util.JsonUtils;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class Main {
    private static final String FILE_URL = "https://data.police.uk/api/crimes-street/all-crime?lat=52.629729&lng=-1.131592";
    private static final String FILE_NAME = "D:\\Vlad_Dubiaga_BigDataLab\\Task3\\test.txt";

    public static void main(String[] args) throws IOException {
        URL url = new URL(FILE_URL);
        List<Crime> list = JsonUtils.parseUrl(url);
        System.out.println(list.get(0));
        System.out.println(list.get(1));

    }

}
