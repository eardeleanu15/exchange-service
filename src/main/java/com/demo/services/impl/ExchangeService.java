package com.demo.services.impl;

import com.demo.exceptions.ExchangeServiceException;
import com.demo.model.Exchange;
import com.demo.repository.IExchangeRatesRepository;
import com.demo.repository.impl.InMemoryExchangeRatesRepository;
import com.demo.services.IExchangeService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ExchangeService implements IExchangeService {

    private final IExchangeRatesRepository exchangeRatesRepository = InMemoryExchangeRatesRepository.getInstance();

    @Override
    public Exchange getExchangeRate(String currency, LocalDate date) throws ExchangeServiceException {
        double rate = exchangeRatesRepository.getRate(date, currency);
        return new Exchange(currency.toUpperCase(), rate);
    }
}
