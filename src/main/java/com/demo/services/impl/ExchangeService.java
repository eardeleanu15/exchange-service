package com.demo.services.impl;

import com.demo.exceptions.ExchangeServiceException;
import com.demo.model.Exchange;
import com.demo.model.ExchangeRates;
import com.demo.services.IExchangeService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class ExchangeService implements IExchangeService {

    private final ExchangeRates exchangeRates = ExchangeRates.getInstance();

    @Override
    public Exchange getExchangeRate(String currency, LocalDate date) throws ExchangeServiceException {
        double rate = exchangeRates.getRate(date, currency);
        return new Exchange(currency.toUpperCase(), rate);
    }
}
