package ru.liga.forecast.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.liga.forecast.algorithms.ForecastAlgorithm;
import ru.liga.forecast.model.Rate;
import ru.liga.repository.RatesRepository;

import java.util.Date;
import java.util.List;

public class RateForecastServiceImpl implements RateForecastService {
    private Logger logger = LoggerFactory.getLogger(RateForecastServiceImpl.class);

    private final ForecastAlgorithm algorithm;
    private final RatesRepository repository;

    public RateForecastServiceImpl(RatesRepository repository, ForecastAlgorithm algorithm) {
        this.repository = repository;
        this.algorithm = algorithm;
    }

    /**
     * Расчет прогнозных курсов
     *
     * @param currency          - валюта
     * @param forecastStartDate - дата начала прогнозирования
     * @param numDays           - число прогнозируемых дней
     * @return список прогнозных курсов
     */
    public List<Rate> calculateForecastRates(String currency, Date forecastStartDate, int numDays) {
        // List<Rate> listHistRates = reader.readCurrencyRates(currency);
        return algorithm.calculateForecastRates(repository.getRates(currency), forecastStartDate, numDays);
    }
}
