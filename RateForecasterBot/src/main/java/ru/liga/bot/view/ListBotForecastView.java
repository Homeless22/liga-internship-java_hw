package ru.liga.bot.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.liga.forecast.model.Rate;
import ru.liga.forecast.view.ForecastView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class ListBotForecastView implements ForecastView {
    private Logger logger = LoggerFactory.getLogger(ListBotForecastView.class);

    final private AbsSender absSender;
    final private Long chatId;

    public ListBotForecastView(AbsSender absSender, Long chatId) {
        this.absSender = absSender;
        this.chatId = chatId;
    }

    @Override
    public void render(Map<String, List<Rate>> currencyRates) {
        sendAnswer(currencyRates);
    }

    private void sendAnswer(Map<String, List<Rate>> currencyRates) {
        try {
            SendMessage answer = new SendMessage();
            answer.setChatId(chatId.toString());

            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, List<Rate>> entry : currencyRates.entrySet()) {
                if (currencyRates.size() > 1) {
                    sb.append(entry.getKey() + "\n");
                }
                for (Rate r : entry.getValue()) {
                    sb.append(String.format("%s - %.2f\n", new SimpleDateFormat("EE dd.MM.yyyy").format(r.getRateDate()), (double) Math.round(r.getRate() * 100) / 100));
                }
                answer.setText(sb.toString());
            }
            absSender.execute(answer);
        } catch (
                TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
