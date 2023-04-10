package ru.liga.forecast.algorithms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.liga.forecast.model.Rate;
import ru.liga.forecast.model.RateDateComparator;

import java.util.*;

public class MysticalForecastAlgorithm implements ForecastAlgorithm {
    private Logger logger = LoggerFactory.getLogger(MysticalForecastAlgorithm.class);

    /**
     * Расчет курсов по мистическому алгоритму
     *
     * @param histExchangeRates - список исторических курсов
     * @param forecastStartDate - дата начала прогнозирования
     * @param numForecastDays   - число прогнозируемых дней
     * @return список прогнозных курсов
     */
    public List<Rate> calculateForecastRates(List<Rate> histExchangeRates, Date forecastStartDate, int numForecastDays) {
        logger.debug("Старт прогноза по мистическому алгоритму");
        logger.debug("Начальная дата прогонозирования: {}", forecastStartDate);
        logger.debug("Число прогнозируемых дней: {}", numForecastDays);
        List<Rate> histRates = new ArrayList<>(histExchangeRates);
        List<Rate> forecastRates = new ArrayList<>();
        histRates.sort(new RateDateComparator());

        Date forecastDate = forecastStartDate;
        int minYear = getMinYear(histRates);
        logger.debug("Минимальный год: {}", minYear);
        int maxYear = getMaxYear(histRates);
        logger.debug("Максимальный год: {}", maxYear);

        Collections.reverse(histRates);
        for (int i = 0; i < numForecastDays; i++) {
            forecastRates.add(new Rate(1.0, findRateByDate(histRates, setYear(forecastDate, getRandomYear(minYear, Math.min(maxYear, getYear(forecastDate) - 1)))), forecastDate, ""));
            forecastDate = nextDate(forecastDate);
        }
        logger.debug("Прогноз по мистическому алгоритму завершен");
        return forecastRates;
    }

    private int getRandomYear(int minYear, int maxYear) {
        return (int) ((Math.random() * (maxYear - minYear)) + minYear);
    }

    private Date setYear(Date date, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();
    }

    private int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    private int getMinYear(List<Rate> histRates) {
        return getYear(histRates.get(0).getRateDate());
    }

    private int getMaxYear(List<Rate> histRates) {
        return getYear(histRates.get(histRates.size() - 1).getRateDate());
    }

    private Date nextDate(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date); //устанавливаем дату, с которой будет производить операции
        instance.add(Calendar.DAY_OF_MONTH, 1);// прибавляем 1 день к установленной дате
        return instance.getTime(); // получаем измененную дату
    }

    private double findRateByDate(List<Rate> rates, Date date) {

        double rate = -1.0;
        for (Rate r : rates) {
            if (r.getRateDate().compareTo(date) <= 0) {
                rate = r.getRate() / r.getNominal();
                break;
            }
        }
        return rate;
    }
}
