package com.demo.services.impl;

import com.demo.exceptions.ExchangeServiceException;
import com.demo.model.Exchange;
import com.demo.repository.IExchangeRatesRepository;
import com.demo.repository.impl.InMemoryExchangeRatesRepository;
import com.demo.services.IExchangeService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Service class used by REST Controllers
 * to handle exchange rates requests.
 */
@Service
public class ExchangeService implements IExchangeService {

    // Retrieve instance of InMemoryExchangeRatesRepository
    private final IExchangeRatesRepository exchangeRatesRepository = InMemoryExchangeRatesRepository.getInstance();

    /**
     * Method used to return an Exchange object
     * , which contains currency and rate values,
     * based on a currency and date parameters.
     * @param currency
     * @param date
     * @return Exchange
     * @throws ExchangeServiceException
     */
    @Override
    public Exchange getExchangeRate(String currency, LocalDate date) throws ExchangeServiceException {
        double rate = exchangeRatesRepository.getRate(date, currency);
        return new Exchange(currency.toUpperCase(), rate);
    }
}
