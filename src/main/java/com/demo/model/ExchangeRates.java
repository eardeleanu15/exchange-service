package com.demo.model;

import com.demo.exceptions.ExchangeServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static com.demo.utils.Constants.DATE_RATES_NOT_FOUND;
import static com.demo.utils.Constants.CURRENCY_RATE_NOT_FOUND;

public class ExchangeRates {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRates.class);

    private static ExchangeRates instance = null;
    private Map<LocalDate,Map<String, Double>> rates;

    private ExchangeRates(){
        this.rates = new HashMap<>();
    }

    public static ExchangeRates getInstance() {
        if (instance == null) {
            synchronized (ExchangeRates.class) {
                if (instance == null) {
                    instance = new ExchangeRates();
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
