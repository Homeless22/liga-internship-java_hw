package ru.liga.repository;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.liga.forecast.model.Rate;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class CsvRatesReader implements RatesReader {
    private Logger logger = LoggerFactory.getLogger(CsvRatesReader.class);

    /**
     * Загрузка курсов валют из файла CSV
     *
     * @param currency - валюта
     * @return список прогнозных курсов
     */
    @Override
    public List<Rate> readRates(String currency) {
        logger.debug("Старт загрузки данных. Валюта: {}", currency);
        List<Rate> rates = loadRatesFromFile(currency);
        logger.debug("Данные загружены");
        return rates;
    }

    final private static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");

    /**
     * Загрузка данных из файлв
     *
     * @param currency       Валюта
     * @param rateDateTo     Максимальная дата курса
     * @param numRecentRates Количество загружаемых курсов валют
     * @return Список курсов
     */
    private List<Rate> loadRatesFromFile(String currency/*, Date rateDateTo, int numRecentRates*/) {
        List<Rate> rates = new LinkedList<>();
        String fileName = "rates/" + currency.toUpperCase() + ".csv";
        logger.debug("Имя файла: {}", fileName);

        Reader reader = new InputStreamReader(getFileFromResourceAsStream(fileName), Charset.forName("utf-8"));

        try (CSVParser csvParser = new CSVParser(reader, buildCSVFormat())) {
            List<CSVRecord> csvRecords = csvParser.getRecords();

            //csvRecords.sort(new CsvRecordComparator());

            for (CSVRecord csvRecord : csvRecords) {
                rates.add(newRate(csvRecord));
            }
            logger.debug("Загружено записей: {}: ", rates.size());
        } catch (IOException e) {
            logger.error("Ошибка загрузки файла {}: ", fileName, e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
        return rates;
    }

    private CSVFormat buildCSVFormat() {
        final boolean skipHeaderRecord = true;
        final String header = "nominal;date;rate;cdx";
        final String delim = ";";
        final String recordSeparator = "\n";
        final char quote = '"';
        final boolean allowMissingColumnNames = true;

        return CSVFormat.Builder.create()
                .setSkipHeaderRecord(skipHeaderRecord)
                .setHeader(header.split(delim))
                .setAllowMissingColumnNames(allowMissingColumnNames)
                .setDelimiter(delim)
                .setRecordSeparator(recordSeparator)
                .setQuote(quote)
                .build();
    }

    private InputStream getFileFromResourceAsStream(String fileName) {

        InputStream inputStream = RatesReader.class.getClassLoader().getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }

    private Rate newRate(CSVRecord csvRecord) {
        try {
            return new Rate(
                    Double.parseDouble(csvRecord.get("nominal").replace(",", ".").replace(" ", "")),
                    Double.parseDouble(csvRecord.get("rate").replace(",", ".").replace(" ", "")),
                    dateFormatter.parse(csvRecord.get("date")),
                    csvRecord.get("cdx"));
        } catch (ParseException e) {
            System.out.println("Invalid date format");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private boolean isEnoughRatesForForecast(int numCurrRate, int numRecentRates) {
        return (numCurrRate >= numRecentRates && numRecentRates >= 0);
    }

    private boolean useCurrRateForForecast(Date rateDate, Date forecastDate) {
        return (rateDate != null && (rateDate.compareTo(forecastDate) <= 0 || forecastDate == null));
    }
}
