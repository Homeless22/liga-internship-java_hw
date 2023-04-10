package ru.liga.enums;

public enum CommandOption {
    COMMAND(null),
    CURRENCY(null),
    DATE("date"),
    PERIOD("period"),
    ALG("alg"),
    OUTPUT("output");

    final private String identifier;
    //final private String description;

    CommandOption(String identifier/*, String description*/) {
        this.identifier = identifier;
      // this.description = description;
    }

    public String getIdentifier() {
        return identifier;
    }
//    public String getDescription() {
//        return description;
//    }
}