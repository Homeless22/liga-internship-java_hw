package ru.liga.bot.command.custom;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Обработка сообщения, не являющегося командой (т.е. обычного текста не начинающегося с "/")
 */
public abstract class CustomBotCommand {
    private final String commandIdentifier;
    private final String description;

    public CustomBotCommand(String commandIdentifier, String description) {
        this.commandIdentifier = commandIdentifier.toLowerCase();
        this.description = description;
    }

    public final String getCommandIdentifier() {
        return this.commandIdentifier;
    }

    public final String getDescription() {
        return this.description;
    }

    public abstract void execute(AbsSender absSender, Chat chat, String text);
}
