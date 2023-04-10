package ru.liga.repository;

import ru.liga.forecast.model.Rate;

import java.util.List;

public interface RatesReader {
    List<Rate> readRates(String currency);
}
