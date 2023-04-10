package ru.liga.bot.command.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Команда "Старт"
 */
public class StartCommand extends ServiceCommand {
    private Logger logger = LoggerFactory.getLogger(StartCommand.class);

    public StartCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        // String userName = Utils.getUserName(user);

        logger.info("Пользователь {}. Начато выполнение команды {}", user.getUserName(), this.getCommandIdentifier());
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(), "Привет!\nЯ - бот-предсказатель курса валют!\nЕсли Вам нужна помощь, нажмите /help");
        logger.info("Пользователь {}. Завершено выполнение команды {}", user.getUserName(), this.getCommandIdentifier());
    }
}