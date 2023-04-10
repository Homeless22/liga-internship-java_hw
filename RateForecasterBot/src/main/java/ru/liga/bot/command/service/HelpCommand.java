package ru.liga.bot.command.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Команда "Помощь"
 */
public class HelpCommand extends ServiceCommand {
    private Logger logger = LoggerFactory.getLogger(HelpCommand.class);

    public HelpCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        logger.info("Пользователь {}. Начато выполнение команды {}", user.getUserName(), this.getCommandIdentifier());
        sendAnswer(absSender, chat.getId(), user.getUserName(), this.getCommandIdentifier(),
                "Команды телеграмм бота:\n" +
                        "Курс валюты на завтра по определенному алгоритму:\n" +
                        "rate TRY -date tomorrow -alg mist\n\n" +
                        "Курс на конкретную дату в будущем по определенному алгоритму:\n" +
                        "rate TRY -date 22.02.2030 -alg mist\n\n" +
                        "Курс валюты на период недели или месяца по определенному алгоритму с выводом в список\n" +
                        "rate USD -period week -alg mist -output list\n\n" +
                        "Курс одной или нескольких валют (до 5) на период недели или месяца по определенному алгоритму с выводом как картинка-график\n" +
                        "rate USD,TRY -period month -alg moon -output graph");
        logger.info("\"Пользователь {}. Завершено выполнение команды {}", user.getUserName(), this.getCommandIdentifier());
        ;
    }
}