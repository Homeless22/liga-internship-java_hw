package ru.liga.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.liga.RateForecasterBotApp;
import ru.liga.bot.command.custom.CustomBotCommandFactory;
import ru.liga.bot.command.service.ExitCommand;
import ru.liga.bot.command.service.HelpCommand;
import ru.liga.bot.command.service.StartCommand;

public final class RateForecasterBot extends TelegramLongPollingCommandBot {
    private Logger logger = LoggerFactory.getLogger(RateForecasterBotApp.class);
    BotConfig botConfig;

    public RateForecasterBot() {
        botConfig = new BotConfig();
        register(new StartCommand("start", "Старт"));
        register(new HelpCommand("help", "Помощь"));
        register(new ExitCommand("exit", "Выход"));
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        Message message = update.getMessage();
        String userName = message.getChat().getUserName();
        String command = message.getText();
        try {
            logger.info("Пользователь {} ввел команду {}", userName, command);
             new CustomBotCommandFactory().createCustomCommand(message.getText()).execute(this, message.getChat(), message.getText());
            logger.info("Команда {} пользователя {} выполнена", userName, command);
        } catch (IllegalArgumentException e) {
            logger.error("Пользователь {}. Ошибка: {}", userName, e.getMessage());
            sendAnswer(message.getChatId(), "Введена неверная команда");
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    private void sendAnswer(Long chatId, String text) {
        SendMessage answer = new SendMessage();
        answer.setText(text);
        answer.setChatId(chatId.toString());
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            //  logger.error(String.format("Ошибка %s. Сообщение, не являющееся командой. Пользователь: %s", e.getMessage(),
            //          userName));
            e.printStackTrace();
        }
    }


}
