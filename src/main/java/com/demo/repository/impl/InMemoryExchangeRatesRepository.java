package com.demo.repository.impl;

import com.demo.exceptions.ExchangeServiceException;
import com.demo.repository.IExchangeRatesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static com.demo.utils.Constants.DATE_RATES_NOT_FOUND;
import static com.demo.utils.Constants.CURRENCY_RATE_NOT_FOUND;

public class InMemoryExchangeRatesRepository implements IExchangeRatesRepository {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryExchangeRatesRepository.class);

    private static IExchangeRatesRepository instance = null;
    private Map<LocalDate,Map<String, Double>> rates;

    private InMemoryExchangeRatesRepository(){
        this.rates = new HashMap<>();
    }

    public static IExchangeRatesRepository getInstance() {
        if (instance == null) {
            synchronized (InMemoryExchangeRatesRepository.class) {
                if (instance == null) {
                    instance = new InMemoryExchangeRatesRepository();
                }
            }
        }
        return instance;
    }

    public synchronized void addDailyRates(LocalDate date, Map<String, Double> rates){
        if (this.rates.get(date) == null) {
            this.rates.put(date, rates);
        } else {
            logger.info("Rates for date - {} - already exists", date.toString());
        }
    }

    public synchronized double getRate(LocalDate date, String currency) throws ExchangeServiceException {
        Double rate;
        Map<String, Double> rates = this.rates.get(date);
        if (rates != null){
            rate = rates.get(currency.toUpperCase());
            if (rate == null) {
                throw new ExchangeServiceException(CURRENCY_RATE_NOT_FOUND + currency);
            }
        } else {
            throw new ExchangeServiceException(DATE_RATES_NOT_FOUND + date.toString());
        }
        return rate;
    }
}
