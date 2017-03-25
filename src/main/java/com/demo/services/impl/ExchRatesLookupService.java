package com.demo.services.impl;

import com.demo.services.IExchRatesLookupService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExchRatesLookupService implements IExchRatesLookupService{

    @Value("${daily.rates.url}")
    private String DAILY_RATES_URL;

    private final RestTemplate restTemplate;

    public ExchRatesLookupService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public String findDailyRates() {
        return restTemplate.getForObject(DAILY_RATES_URL, String.class);
    }
}
