package ru.liga;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.liga.bot.RateForecasterBot;

import java.util.logging.LogManager;

public class RateForecasterBotApp {
    private static Logger logger = LoggerFactory.getLogger(RateForecasterBotApp.class);

    public static void main(String[] args) {
        logger.info(String.format("Старт приложения"));

        RateForecasterBot bot = new RateForecasterBot();
        try {
            new TelegramBotsApi(DefaultBotSession.class).registerBot(bot);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }
}