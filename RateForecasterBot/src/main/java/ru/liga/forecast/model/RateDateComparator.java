package ru.liga.forecast.model;


import java.util.Comparator;

public class RateDateComparator implements Comparator<Rate> {
    public int compare(Rate r1, Rate r2) {
        return r1.getRateDate().compareTo(r2.getRateDate());
    }
}

