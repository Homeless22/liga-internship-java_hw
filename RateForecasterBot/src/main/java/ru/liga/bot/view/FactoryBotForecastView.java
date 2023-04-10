package ru.liga.bot.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.liga.bot.command.custom.RateBotCommand;
import ru.liga.enums.DataOutput;
import ru.liga.forecast.view.FactoryForecastView;
import ru.liga.forecast.view.ForecastView;

public class FactoryBotForecastView implements FactoryForecastView {
    private Logger logger = LoggerFactory.getLogger(FactoryBotForecastView.class);

    final private AbsSender absSender;
    final private Long chatId;

    public FactoryBotForecastView(AbsSender absSender, Long chatId){
        this.absSender = absSender;
        this.chatId = chatId;
    }

    @Override
    public ForecastView createForecastView(String output) {
        if (output.equalsIgnoreCase(DataOutput.LIST.getIdentifier())) {
            return new ListBotForecastView(absSender, chatId);
        } else if (output.equalsIgnoreCase(DataOutput.GRAPH.getIdentifier())) {
            return new GraphBotForecastView(absSender, chatId);
        } else {
            throw new IllegalArgumentException("Invalid output type");
        }
    }
}
