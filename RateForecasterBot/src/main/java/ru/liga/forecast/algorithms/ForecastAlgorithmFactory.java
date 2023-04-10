package ru.liga.forecast.algorithms;

import ru.liga.enums.Algorithm;

public class ForecastAlgorithmFactory {
    public ForecastAlgorithm createForecastAlgorithm(String algorithm) {
        if (algorithm.equalsIgnoreCase(Algorithm.AVG.getIdentifier())) {
            return new AvgForecastAlgorithm();
        } else if (algorithm.equalsIgnoreCase(Algorithm.YEAR.getIdentifier())) {
            return new LastYearForecastAlgorithm();
        } else if (algorithm.equalsIgnoreCase(Algorithm.MIST.getIdentifier())) {
            return new MysticalForecastAlgorithm();
        } else if (algorithm.equalsIgnoreCase(Algorithm.LIN.getIdentifier())) {
            return new LinearRegressionForecastAlgorithm();
        } else {
            throw new IllegalArgumentException("Invalid аlgorithm");
        }
    }
}
