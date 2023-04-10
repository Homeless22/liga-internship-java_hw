package ru.liga.bot.command.custom;

import static ru.liga.enums.Command.RATE;

public class CustomBotCommandFactory {
    /**
     * Метод получения объекта пользовательской команды
     *
     * @param command - команда пользователя
     * @return объект пользовательской команды
     */
    public CustomBotCommand createCustomCommand(String command) {
        if (command.trim().split(" ")[0].equalsIgnoreCase(RATE.getIdentifier())) {
            return new RateBotCommand(command);
        } else {
            throw new IllegalArgumentException("Invalid command");
        }
    }
}
