package com.demo.jobs;

import com.demo.services.IExchangeRatesLookupService;
import com.demo.utils.RatesXmlParser;
import lombok.extern.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

import static com.demo.utils.Constants.DAILY_RATES_NOT_RETRIEVED;

/**
 * One time job that executes after application
 * context is initialized that retrieves historical
 * exchange rates (up to 90 days in the past from current date)
 */
@Component
@Log4j
public class HistoricalRatesJob implements CommandLineRunner{

    @Autowired
    private final IExchangeRatesLookupService exchangeRatesLookupService;

    public HistoricalRatesJob(IExchangeRatesLookupService exchangeRatesLookupService) {
        this.exchangeRatesLookupService = exchangeRatesLookupService;
    }

    @Override
    public void run(String... strings) throws Exception {
        // Initialize  findHistoricalRates method to run asynchronously
        CompletableFuture<String> completableFuture =  CompletableFuture.supplyAsync(exchangeRatesLookupService::findHistoricalRates);
        // Add success callback
        CompletableFuture<Void> future = completableFuture.thenApply(results -> RatesXmlParser.extractHistoricalRates(results));

        try {
            // execute async method
            future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error(DAILY_RATES_NOT_RETRIEVED);
        } catch (Exception e) {
            log.error(DAILY_RATES_NOT_RETRIEVED);
        }
    }
}
