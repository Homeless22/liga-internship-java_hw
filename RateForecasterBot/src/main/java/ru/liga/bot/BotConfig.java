package ru.liga.bot;

public class BotConfig {
    final private String botName = "@ExchangeRateForecastBot";
    final private String botToken = "5999661089:AAEnBirt033UTEWD243YgWD17H4Y2pCXem8";

    /**
     * Метод получения имени бота
     *
     * @return имя бота
     */
    public String getBotUsername() {
        return botName;
    }

    /**
     * Метод получения токена бота
     *
     * @return токен бота
     */
    public String getBotToken() {
        return botToken;
    }
}
