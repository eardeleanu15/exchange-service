package com.demo.repository;

import com.demo.exceptions.ExchangeServiceException;

import java.time.LocalDate;
import java.util.Map;

public interface IExchangeRatesRepository {

    void addDailyRates(LocalDate date, Map<String, Double> rates);

    double getRate(LocalDate date, String currency) throws ExchangeServiceException;

}
