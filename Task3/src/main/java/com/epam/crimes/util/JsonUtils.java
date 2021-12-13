package com.epam.crimes.util;

import com.epam.crimes.entity.Crime;
import com.google.gson.*;
import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class JsonUtils {

    public static List<Crime> parseUrl(URL url) {
        String content = "";
        if (url == null) {
            return null; //TODO исправить
        }
        Crime[] crimes = null;
        try (BufferedInputStream in = new BufferedInputStream(url.openStream())) {
            content = IOUtils.toString(in, "UTF-8");
            crimes = new GsonBuilder().setDateFormat("yyyy-MM").create().fromJson(content, Crime[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (crimes != null) {
            return Arrays.asList(crimes);
        } else {
            return null;
        }
    }


}
