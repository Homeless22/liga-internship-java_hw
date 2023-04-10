package ru.liga.enums;

public enum Algorithm {
    AVG("avg"),
    YEAR("year"),
    MIST("mist"),
    LIN("lin");

    final private String identifier;
    //final private String description;

    Algorithm(String identifier/*, String description*/) {
        this.identifier = identifier;
        // this.description = description;
    }

    public String getIdentifier() {
        return identifier;
    }

}
