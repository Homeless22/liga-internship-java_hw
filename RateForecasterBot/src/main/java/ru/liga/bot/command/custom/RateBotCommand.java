package ru.liga.bot.command.custom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.liga.bot.view.FactoryBotForecastView;
import ru.liga.command.RateCommand;
import ru.liga.repository.CsvRatesRepository;

import static ru.liga.enums.Command.RATE;


/**
 * Команда "Курс"
 */
public class RateBotCommand extends CustomBotCommand {
    private Logger logger = LoggerFactory.getLogger(RateBotCommand.class);

    private final String beginNamedOption = "-";

    String command;

    public RateBotCommand(String command) {
        super(RATE.getIdentifier(), RATE.getDescription());
        this.command = command;
    }

    /**
     * Выполнение команды
     *
     * @param absSender
     * @param chat
     * @param command
     */
    @Override
    public void execute(AbsSender absSender, Chat chat, String command) {
        logger.debug("Пользователь {}. Начато выполнение команды {}", chat.getUserName(), command);
        new RateCommand(command, new CsvRatesRepository(), new FactoryBotForecastView(absSender, chat.getId())).execute();
        logger.debug("Пользователь {}. Завершено выполнение команды {}", chat.getUserName(), command);
    }
}