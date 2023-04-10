package ru.liga.repository;

import ru.liga.forecast.model.Rate;

import java.util.List;

public interface RatesRepository {

    List<Rate> getRates(String currency);
}
