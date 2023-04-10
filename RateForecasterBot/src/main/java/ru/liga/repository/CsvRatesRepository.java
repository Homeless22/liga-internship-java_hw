package ru.liga.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.liga.forecast.model.Rate;

import java.util.List;

public class CsvRatesRepository implements RatesRepository {
    private Logger logger = LoggerFactory.getLogger(CsvRatesRepository.class);

    /**
     * Получение списка курсов из источника
     *
     * @param currency - валюта
     * @return список прогнозных курсов
     */
    @Override
    public List<Rate> getRates(String currency) {
        return new CsvRatesReader().readRates(currency);
    }
}
