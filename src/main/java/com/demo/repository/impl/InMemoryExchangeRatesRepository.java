package com.demo.repository.impl;

import com.demo.exceptions.ExchangeServiceException;
import com.demo.repository.IExchangeRatesRepository;
import lombok.extern.log4j.Log4j;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.demo.utils.Constants.CURRENCY_RATE_NOT_FOUND;
import static com.demo.utils.Constants.DATE_RATES_NOT_FOUND;

/**
 * Singleton class that holds, in memory,
 * exchange rates data.
 */
@Log4j
public class InMemoryExchangeRatesRepository implements IExchangeRatesRepository {

    private static volatile IExchangeRatesRepository instance = null;
    private Map<LocalDate,Map<String, Double>> rates;

    private InMemoryExchangeRatesRepository(){
        this.rates = new ConcurrentHashMap<>();
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

    /**
     * Method to add new daily rates to
     * in memory repository.
     * @param date
     * @param rates
     */
    public void addDailyRates(LocalDate date, Map<String, Double> rates){
        if (this.rates.get(date) == null) {
            this.rates.put(date, rates);
            log.info(String.format("Rates for date %s where added in repository", date.toString()));
        } else {
            log.info(String.format("Rates for date %s already exists", date.toString()));
        }
    }

    /**
     * Method to retrieve a specific rate based
     * on a date and currency parameters.
     * If the rates for the requested date parameter are
     * not found in the repository an ExchangeServiceException is thrown.
     * If the reate for the requested currency parameter is not
     * found in the repository an ExchangeServiceException is thrown.
     * @param date
     * @param currency
     * @return
     * @throws ExchangeServiceException
     */
    public double getRate(LocalDate date, String currency) throws ExchangeServiceException {
        Double rate;
        // Get rates for requested date
        Map<String, Double> rates = this.rates.get(date);
        if (rates != null){
            // Get rate for requested currency
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
