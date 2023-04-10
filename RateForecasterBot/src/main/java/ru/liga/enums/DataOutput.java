package ru.liga.enums;

public enum DataOutput {
    LIST("list"),
    GRAPH("graph");

    final private String identifier;

    DataOutput(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}
