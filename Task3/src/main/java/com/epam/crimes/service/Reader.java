package com.epam.crimes.service;

import java.util.List;

public interface Reader {

    List<Double> readCoordinatesFromFile(String path);

}
