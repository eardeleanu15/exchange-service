package com.demo.services;

import java.util.concurrent.Future;

public interface IExchRatesLookupService {

    Future<String> findRates();
}
