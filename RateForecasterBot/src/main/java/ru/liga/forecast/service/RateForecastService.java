package ru.liga.forecast.service;

import ru.liga.forecast.model.Rate;

import java.util.Date;
import java.util.List;

public interface RateForecastService {
    List<Rate> calculateForecastRates(String currency, Date forecastStartDate, int numDays);
}
