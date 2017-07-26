package com.demo.jobs;

import com.demo.services.IExchangeRatesLookupService;
import com.demo.utils.RatesXmlParser;
import lombok.extern.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

import static com.demo.utils.Constants.DAILY_RATES_NOT_RETRIEVED;

/**
 * Scheduled cron job, that executes on
 * a daily basis and retrieves current date
 * exchange rates.
 */
@Component
@Log4j
public class ExchangeRatesDailyJob {

    @Autowired
    private final IExchangeRatesLookupService exchangeRatesLookupService;

    public ExchangeRatesDailyJob(IExchangeRatesLookupService exchangeRatesLookupService) {
        this.exchangeRatesLookupService = exchangeRatesLookupService;
    }



    /**
     * Note: For testing purposes the job
     * is set to run at every 5 minutes from
     * application start-up.
     */
    //    @Scheduled(cron="0 0 12 * * *")
    @Scheduled(cron="0 */5 * * * *")
    public void run() {

        // Initialize findDailyRates method to run asynchronously
        CompletableFuture<String> completableFuture =  CompletableFuture.supplyAsync(exchangeRatesLookupService::findDailyRates);
        // Add success callback
        CompletableFuture<Void> future = completableFuture.thenApply(results -> RatesXmlParser.extractDailyRates(results));

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
