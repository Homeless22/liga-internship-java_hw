package ru.liga.enums;
public enum Command {
    RATE("rate", "Прогноз курса валюты");

    final private String identifier;
    final private String description;

    Command(String identifier, String description) {
        this.identifier = identifier;
        this.description = description;
    }

    public String getIdentifier() {
        return identifier;
    }
    public String getDescription() {
        return description;
    }
}
