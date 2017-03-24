package com.demo.services.impl;

import com.demo.services.IExchRatesLookupService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.concurrent.Future;

import static com.demo.utils.Constants.DAILY_RATES_URL;

@Service
public class ExchRatesLookupService implements IExchRatesLookupService{

    private final RestTemplate restTemplate;

    public ExchRatesLookupService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Async
    @Override
    public Future<String> findRates() {
        String response = restTemplate.getForObject(DAILY_RATES_URL, String.class);
        return new AsyncResult<>(response);
    }
}
