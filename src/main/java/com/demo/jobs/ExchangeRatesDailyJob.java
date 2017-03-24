package com.demo.jobs;

import com.demo.services.IExchRatesLookupService;
import com.demo.utils.RatesXmlParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.Future;

import static com.demo.utils.Constants.DAILY_RATES_NOT_RETRIEVED;

@Component
public class ExchangeRatesDailyJob{

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRatesDailyJob.class);

    @Autowired
    private final IExchRatesLookupService exchangeRatesLookupService;

    public ExchangeRatesDailyJob(IExchRatesLookupService exchangeRatesLookupService) {
        this.exchangeRatesLookupService = exchangeRatesLookupService;
    }

//    @Scheduled(cron="0 0 12 * * *")
    @Scheduled(cron="0 */1 * * * *")
    public void run() {
        Future<String> rates = exchangeRatesLookupService.findRates();

        try {
            while (!rates.isDone()){
                Thread.sleep(10);
            }

            String result = rates.get();
            logger.info("{} result :: {}", this.getClass().getSimpleName(), result);

            if (!StringUtils.isEmpty(result)) {
                // extract daily currency data
                RatesXmlParser.extractDailyRates(result);
            } else {
                logger.error("Daily async call result is null");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error(DAILY_RATES_NOT_RETRIEVED);
        } catch (Exception e) {
            logger.error(DAILY_RATES_NOT_RETRIEVED);
        }

    }


}
