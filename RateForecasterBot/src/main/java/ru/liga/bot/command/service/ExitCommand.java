package ru.liga.bot.command.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class ExitCommand extends ServiceCommand {
    private Logger logger = LoggerFactory.getLogger(StartCommand.class);

    public ExitCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        // String userName = Utils.getUserName(user);

        logger.info("Пользователь {}. Начато выполнение команды {}", user.getUserName(), this.getCommandIdentifier());
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(), "До скорой встречи!");
        logger.info("Пользователь {}. Завершено выполнение команды {}", user.getUserName(), this.getCommandIdentifier())
        ;
    }
}
