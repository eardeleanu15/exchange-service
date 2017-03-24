package com.demo.rest;

import com.demo.exceptions.ExchangeServiceException;
import com.demo.model.Exchange;
import com.demo.services.IExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/rest/exchange")
public class ExchangeResource {

    @Autowired
    private IExchangeService exchangeService;

    @RequestMapping(value = "/rate/{currency}", method = RequestMethod.GET)
    public @ResponseBody Exchange getRate(@PathVariable("currency") String currency, @RequestParam("date") String date)
            throws ExchangeServiceException {
        LocalDate formatDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        return exchangeService.getExchangeRate(currency, formatDate);
    }

}
