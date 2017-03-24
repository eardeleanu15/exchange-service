package com.demo.services;

import com.demo.exceptions.ExchangeServiceException;
import com.demo.model.Exchange;

import java.time.LocalDate;

public interface IExchangeService {

    Exchange getExchangeRate(String currency, LocalDate date) throws ExchangeServiceException;

}
