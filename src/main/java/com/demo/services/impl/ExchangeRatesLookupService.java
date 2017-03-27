package com.demo.services.impl;

import com.demo.services.IExchangeRatesLookupService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExchangeRatesLookupService implements IExchangeRatesLookupService {

    @Value("${daily.rates.url}")
    private String DAILY_RATES_URL;

    @Value("${historical.rates.url}")
    private String HISTORICAL_RATES_URL;

    private final RestTemplate restTemplate;

    public ExchangeRatesLookupService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public String findDailyRates() {
        return restTemplate.getForObject(DAILY_RATES_URL, String.class);
    }

    @Override
    public String findHistoricalRates() {
        System.out.println("Historical Rates URL :: " + HISTORICAL_RATES_URL);
        return restTemplate.getForObject(HISTORICAL_RATES_URL, String.class);
    }
}
