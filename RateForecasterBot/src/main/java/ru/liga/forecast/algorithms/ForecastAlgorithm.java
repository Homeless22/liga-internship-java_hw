package ru.liga.forecast.algorithms;

import ru.liga.forecast.model.Rate;

import java.util.Date;
import java.util.List;

public interface ForecastAlgorithm {
    List<Rate> calculateForecastRates (List<Rate> histExchangeRates, Date forecastStartDate, int numForecastDays);
}
