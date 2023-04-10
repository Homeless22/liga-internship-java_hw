package ru.liga.bot.view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.liga.forecast.model.Rate;
import ru.liga.forecast.view.ForecastView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GraphBotForecastView implements ForecastView {
    private Logger logger = LoggerFactory.getLogger(GraphBotForecastView.class);

    final private AbsSender absSender;
    final private Long chatId;

    public GraphBotForecastView(AbsSender absSender, Long chatId) {
        this.absSender = absSender;
        this.chatId = chatId;
    }

    @Override
    public void render(Map<String, List<Rate>> currencyRates) {
        sendAnswer(currencyRates);
    }

    private void sendAnswer(Map<String, List<Rate>> currencyRates) {
        try {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(chatId.toString());

            BufferedImage chartImage = createChartImage(currencyRates);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(chartImage, "png", outputStream);
            sendPhoto.setPhoto(new InputFile(new ByteArrayInputStream(outputStream.toByteArray()), "chart.png"));

            absSender.execute(sendPhoto);
        } catch (TelegramApiException | IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage createChartImage(Map<String, List<Rate>> mapCurrencyRates) {
        final String CHART_TITLE = "Exchange Rates";
        final String X_AXIS_LABEL = "Day";
        final String Y_AXIS_LABEL = "Rate";

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int numberCurrencies = 0;
        for (Map.Entry<String, List<Rate>> entry : mapCurrencyRates.entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i++) {
                dataset.addValue(entry.getValue().get(i).getRate(), entry.getKey(), Integer.valueOf(i + 1));
            }
            numberCurrencies++;
        }

        JFreeChart chart = ChartFactory.createLineChart(
                CHART_TITLE,
                X_AXIS_LABEL,
                Y_AXIS_LABEL,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        Color[] colors = {Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE};
        for (int i = 0; i < numberCurrencies; i++) {
            chart.getCategoryPlot().getRenderer().setSeriesPaint(i, ((i < colors.length) ? colors[i] : Color.PINK));
        }

        BufferedImage chartImage = chart.createBufferedImage(800, 600);
        return chartImage;
    }
}
