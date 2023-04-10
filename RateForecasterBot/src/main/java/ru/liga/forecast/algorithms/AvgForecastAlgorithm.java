package ru.liga.forecast.algorithms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.liga.forecast.model.Rate;
import ru.liga.forecast.model.RateDateComparator;

import java.util.*;

public class AvgForecastAlgorithm implements ForecastAlgorithm {
    private Logger logger = LoggerFactory.getLogger(AvgForecastAlgorithm.class);

    private final int maxNumHistoricalRates = 7;

    /**
     * Расчет курсов по алгоритму по средним значениям
     *
     * @param histExchangeRates - список исторических курсов
     * @param forecastStartDate - дата начала прогнозирования
     * @param numForecastDays   - число прогнозируемых дней
     * @return список прогнозных курсов
     */
    @Override
    public List<Rate> calculateForecastRates(List<Rate> histExchangeRates, Date forecastStartDate, int numForecastDays) {
        logger.debug("Старт прогноза по средним значениям");
        logger.debug("Начальная дата прогонозирования: {}", forecastStartDate);
        logger.debug("Число прогнозируемых дней: {}", numForecastDays);
        List<Rate> forecastRates = new ArrayList<>();
        List<Rate> histRates = getHistRatesForForecasting(histExchangeRates, forecastStartDate);

        //отбор предыдущих курсов на дату refDate
        Date forecastDate = forecastStartDate;

        for (int i = 0; i < numForecastDays; i++) {
            Rate rate = new Rate(1, calculateAverageRate(histRates), forecastDate, "");
            //добавляем прогнозный курс в список с прогнозными курсами
            forecastRates.add(rate);

            //удаляем самый ранний исторический курс из списка
            if (histRates.size() >= maxNumHistoricalRates) {
                histRates.remove(histRates.size() - 1);
            }

            //добавляем прогнозный курс для следующего расчета
            histRates.add(0, rate);

            //следущая прогнозная дата
            forecastDate = nextDate(forecastDate);
        }
        logger.debug("Прогноз по средним значениям выполнен");
        return forecastRates;
    }

    private List<Rate> getHistRatesForForecasting(List<Rate> histRates, Date forecastStartDate) {
        List<Rate> rates = new ArrayList<>(histRates);
        rates.sort(new RateDateComparator());
        Collections.reverse(rates);
        for (int i = 0; i < histRates.size(); i++) {
            if (histRates.get(i).getRateDate().compareTo(forecastStartDate) < 0) {
                return rates.subList(i, i + maxNumHistoricalRates);
            }
        }
        return null;
    }

    private double calculateAverageRate(List<Rate> rates) {
        Double sumRates = 0.0;
        for (Rate r : rates) {
            sumRates += r.getRate() / r.getNominal();
        }
        return (sumRates == 0.0) ? sumRates : (sumRates / rates.size());
    }

    private Date nextDate(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date); //устанавливаем дату, с которой будет производить операции
        instance.add(Calendar.DAY_OF_MONTH, 1);// прибавляем 1 день к установленной дате
        return instance.getTime(); // получаем измененную дату
    }
}
