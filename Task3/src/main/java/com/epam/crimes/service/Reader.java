package com.epam.crimes.service;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;


public interface Reader {

    ConcurrentLinkedQueue<List<Double>> readCoordinatesFromFile(String path);

}
