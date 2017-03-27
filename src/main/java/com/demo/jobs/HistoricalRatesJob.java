package com.demo.jobs;

import com.demo.services.IExchangeRatesLookupService;
import com.demo.utils.RatesXmlParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

import static com.demo.utils.Constants.DAILY_RATES_NOT_RETRIEVED;

@Component
public class HistoricalRatesJob implements CommandLineRunner{

    private static final Logger logger = LoggerFactory.getLogger(HistoricalRatesJob.class);

    @Autowired
    private final IExchangeRatesLookupService exchangeRatesLookupService;

    public HistoricalRatesJob(IExchangeRatesLookupService exchangeRatesLookupService) {
        this.exchangeRatesLookupService = exchangeRatesLookupService;
    }

    @Override
    public void run(String... strings) throws Exception {
        // Initialize method to run asynchronously
        CompletableFuture<String> completableFuture =  CompletableFuture.supplyAsync(exchangeRatesLookupService::findHistoricalRates);
        // Add success callback
        CompletableFuture<Void> future = completableFuture.thenApply(results -> RatesXmlParser.extractHistoricalRates(results));

        try {
            // execute async method
            future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error(DAILY_RATES_NOT_RETRIEVED);
        } catch (Exception e) {
            logger.error(DAILY_RATES_NOT_RETRIEVED);
        }
    }
}