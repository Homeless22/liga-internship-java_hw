package ru.liga.forecast.algorithms;

import edu.princeton.cs.algs4.LinearRegression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.liga.forecast.model.Rate;
import ru.liga.forecast.model.RateDateComparator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LinearRegressionForecastAlgorithm implements ForecastAlgorithm {

    private Logger logger = LoggerFactory.getLogger(LinearRegressionForecastAlgorithm.class);

    public static final long millisecondsPerDay = 86400000L;
    private final int maxNumHistoricalRates = 30;

    /**
     * Расчет курсов по алгоритму линейной регрессии
     *
     * @param histExchangeRates - список исторических курсов
     * @param forecastStartDate - дата начала прогнозирования
     * @param numForecastDays   - число прогнозируемых дней
     * @return список прогнозных курсов
     */
    public List<Rate> calculateForecastRates(List<Rate> histExchangeRates, Date forecastStartDate, int numForecastDays) {
        logger.debug("Старт прогноза по алгоритму линейной регрессии");
        logger.debug("Начальная дата прогонозирования: {}", forecastStartDate);
        logger.debug("Число прогнозируемых дней: {}", numForecastDays);
        List<Rate> rates = getHistRatesForForecasting(histExchangeRates, forecastStartDate);
        List<Rate> forecastRates = new ArrayList<>();

        double[] x = new double[rates.size()];
        double[] y = new double[rates.size()];
        for (int i = 0; i < rates.size(); i++) {
            x[i] = dateToDays(rates.get(i).getRateDate());
            y[i] = rates.get(i).getRate() / rates.get(i).getNominal();
        }
        LinearRegression linearRegression = new LinearRegression(x, y);

        Date forecastDate = forecastStartDate;
        for (int i = 0; i < numForecastDays; i++) {
            forecastRates.add(new Rate(1, linearRegression.predict(dateToDays(forecastDate)), forecastDate, ""));
            forecastDate = nextDate(forecastDate);
        }
        logger.debug("Прогноз по алгоритму линейной регрессии завершен");
        return forecastRates;
    }

    List<Rate> getHistRatesForForecasting(List<Rate> histRates, Date forecastStartDate) {
        List<Rate> rates = new ArrayList<>(histRates);
        rates.sort(new RateDateComparator());
        for (int i = histRates.size() - 1; i >= 0; i--) {
            if (histRates.get(i).getRateDate().compareTo(forecastStartDate) < 0) {
                return rates.subList(i - maxNumHistoricalRates, i);
            }
        }
        return null;
    }

    private int dateToDays(Date date) {
        //  convert a date to an integer and back again
        long currentTime = date.getTime();
        currentTime = currentTime / millisecondsPerDay;
        return (int) currentTime;
    }

    private Date nextDate(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date); //устанавливаем дату, с которой будет производить операции
        instance.add(Calendar.DAY_OF_MONTH, 1);// прибавляем 1 день к установленной дате
        return instance.getTime(); // получаем измененную дату
    }
}
