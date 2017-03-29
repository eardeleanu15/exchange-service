package com.demo.rest;

import com.demo.exceptions.ExchangeServiceException;
import com.demo.services.IExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.demo.utils.Constants.RATE_DATE_MALFORMED_FORMAT;

@RestController
@RequestMapping("/rest/exchange")
public class ExchangeResource {

    @Autowired
    private IExchangeService exchangeService;

    @RequestMapping(value = "/rate/{currency}", method = RequestMethod.GET)
    public ResponseEntity getRate(@PathVariable("currency") String currency, @RequestParam("date") String date)
            throws ExchangeServiceException {
        LocalDate formatDate = null;
        try {
            // Request date should be of format 'YYYY-MM-DD'
            formatDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch(DateTimeParseException e) {
            // Bad Request date format
            // Setting HTTP Status to 400 - Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RATE_DATE_MALFORMED_FORMAT);
        }

        return ResponseEntity.ok(exchangeService.getExchangeRate(currency, formatDate));
    }

}
