package ru.liga.command;

import ru.liga.enums.CommandOption;
import ru.liga.enums.DataOutput;
import ru.liga.enums.ForecastPeriod;
import ru.liga.forecast.service.RateForecastService;
import ru.liga.forecast.service.RateForecastServiceImpl;
import ru.liga.forecast.view.FactoryForecastView;
import ru.liga.forecast.algorithms.ForecastAlgorithmFactory;
import ru.liga.forecast.model.Rate;
import ru.liga.repository.CsvRatesRepository;
import ru.liga.repository.RatesRepository;

import javax.validation.ValidationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.liga.enums.Command.RATE;
import static ru.liga.enums.CommandOption.*;

public class RateCommand {
    private final String beginNamedOption = "-";

    private final String command;
    private final RatesRepository ratesRepository;
    private final FactoryForecastView factoryForecastView;
    private Map<CommandOption, String> options = new HashMap<>();

    public RateCommand(String command, RatesRepository ratesRepository, FactoryForecastView factoryForecastView) {
        this.command = command;
        this.ratesRepository = ratesRepository;
        this.factoryForecastView = factoryForecastView;
    }

    public void execute() {
        parseCommand();

        if (isCommandValid()) {
            Date forecaseStartDate = getForecastStartDate();
            int numDays = getNumberForecastDays();

            String[] currencies = options.get(CURRENCY).split(",");

            Map<String, List<Rate>> currencyRates = new HashMap<>();
            for (int i = 0; i < currencies.length; i++) {
                RateForecastService rateForecastService = new RateForecastServiceImpl(ratesRepository, new ForecastAlgorithmFactory().createForecastAlgorithm(options.get(ALG)));
                currencyRates.put(currencies[i], rateForecastService.calculateForecastRates(currencies[i], forecaseStartDate, numDays));
            }
            factoryForecastView.createForecastView(options.get(OUTPUT)).render(currencyRates);
        }
    }

    private boolean isOptionName(String text) {
        return (beginNamedOption.equals(text.substring(0, 1)));
    }

    private void parseCommand() throws ValidationException {
        String[] arrOptions = command.replaceAll("[\\s]{2,}", " ").trim().split(" ");
        if (arrOptions.length < 6 || !RATE.getIdentifier().equalsIgnoreCase(arrOptions[0]) || arrOptions[1].isEmpty()) {
            throw new ValidationException("Неверный формат команды");
        }

        options.put(COMMAND, arrOptions[0].toUpperCase());
        options.put(CURRENCY, arrOptions[1].toUpperCase());

        CommandOption option = null;
        for (int i = 2; i < arrOptions.length; i++) {
            if (isOptionName(arrOptions[i])) {
                String optionName = arrOptions[i].substring(1);
                for (CommandOption o : CommandOption.values()) {
                    if (optionName.equalsIgnoreCase(o.getIdentifier())) {
                        option = o;
                        break;
                    }
                }
                if (option == null) {
                    throw new ValidationException("Указан неверный параметр");
                }
            } else {
                if (option != null) {
                    options.put(option, arrOptions[i]);
                    option = null;
                } else {
                    throw new ValidationException("Указано значение без параметра");
                }
            }
        }

        if (!options.containsKey(OUTPUT)){
            options.put(OUTPUT, DataOutput.LIST.getIdentifier());
        }
    }

    private boolean isCommandValid() throws ValidationException {
        if (options.size() < 4 || options.size() > 5) {
            throw new ValidationException("Задано неверное количество параметров команды");
        }

        if (options.containsKey(PERIOD) && options.containsKey(ForecastPeriod.DATE)) {
            throw new ValidationException("Указаны несовместимые параметры команды");
        }

        if (!options.containsKey(COMMAND)
                || !options.containsKey(CURRENCY)
                || !(options.containsKey(PERIOD) || options.containsKey(CommandOption.DATE))
                || !options.containsKey(ALG)) {
            throw new ValidationException("Не указаны обязательные параметры команды");
        }
        return true;
    }

    private int getNumberForecastDays() {
        int numberForecastDays = -1;

        if (options.containsKey(PERIOD)) {
            for (ForecastPeriod period : ForecastPeriod.values()) {
                if (options.get(PERIOD).equalsIgnoreCase(period.getPeriod())) {
                    numberForecastDays = period.getNumberOfDays();
                }
            }
        } else if (options.containsKey(CommandOption.DATE)) {
            numberForecastDays = ForecastPeriod.DATE.getNumberOfDays();
        }

        return numberForecastDays;
    }

    private Date getForecastStartDate() {
        Date forecastStartDate = new Date();

        if (options.containsKey(CommandOption.DATE)) {
            try {
                forecastStartDate = new SimpleDateFormat("dd.MM.yyyy").parse(options.get(CommandOption.DATE));
            } catch (ParseException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        return forecastStartDate;
    }
}
