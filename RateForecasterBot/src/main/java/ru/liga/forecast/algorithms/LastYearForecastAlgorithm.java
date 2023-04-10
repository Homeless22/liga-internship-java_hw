package ru.liga.forecast.algorithms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.liga.forecast.model.Rate;
import ru.liga.forecast.model.RateDateComparator;

import java.util.*;

public class LastYearForecastAlgorithm implements ForecastAlgorithm {
    private Logger logger = LoggerFactory.getLogger(LastYearForecastAlgorithm.class);

    /**
     * Расчет курсов по алгоритму по значенимс предыдущего года
     *
     * @param histExchangeRates - список исторических курсов
     * @param forecastStartDate - дата начала прогнозирования
     * @param numForecastDays   - число прогнозируемых дней
     * @return список прогнозных курсов
     */
    public List<Rate> calculateForecastRates(List<Rate> histExchangeRates, Date forecastStartDate, int numForecastDays) {
        logger.debug("Старт прогноза по данным за прошлый год");
        logger.debug("Начальная дата прогонозирования: {}", forecastStartDate);
        logger.debug("Число прогнозируемых дней: {}", numForecastDays);
        List<Rate> histRates = new ArrayList<>(histExchangeRates);
        List<Rate> forecastRates = new ArrayList<>();
        Date forecastDate = forecastStartDate;

        histRates.sort(new RateDateComparator());
        Collections.reverse(histRates);
        for (int i = 0; i < numForecastDays; i++) {
            forecastRates.add(new Rate(1.0, findRateFromLastYear(histRates, forecastDate), forecastDate, ""));
            forecastDate = nextDate(forecastDate);
        }
        logger.debug("Прогноз по данным за прошлый год завершен");
        return forecastRates;
    }

    private double findRateFromLastYear(List<Rate> rates, Date date) {
        Date dateFromLastYear = addYear(date, -1);
        double rate = -1.0;
        for (Rate r : rates) {
            if (r.getRateDate().compareTo(dateFromLastYear) <= 0) {
                rate = r.getRate() / r.getNominal();
                break;
            }
        }
        return rate;
    }

    private Date addYear(Date date, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, year);
        return calendar.getTime();
    }

    private Date nextDate(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date); //устанавливаем дату, с которой будет производить операции
        instance.add(Calendar.DAY_OF_MONTH, 1);// прибавляем 1 день к установленной дате
        return instance.getTime(); // получаем измененную дату
    }
}
