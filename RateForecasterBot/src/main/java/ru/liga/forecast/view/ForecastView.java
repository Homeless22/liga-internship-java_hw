package ru.liga.forecast.view;

import ru.liga.forecast.model.Rate;

import java.util.List;
import java.util.Map;
public interface ForecastView {
    void render(Map<String, List<Rate>> data);
}
