package ru.liga.exceptions;

/**
 * Валюта не найдена в справочниках
 */
public class CurrencyNotFoundException extends Exception {
    public CurrencyNotFoundException(String message) {
        super(message);
    }
}
