package com.demo.services;

public interface IExchangeRatesLookupService {

    String findDailyRates();

    String findHistoricalRates();

}
