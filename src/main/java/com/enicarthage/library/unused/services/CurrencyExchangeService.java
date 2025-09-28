package com.enicarthage.library.unused.services;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class CurrencyExchangeService {
    
    private Map<String, Double> exchangeRates;
    
    public CurrencyExchangeService() {
        // Currency exchange service for library international payments
        initializeExchangeRates();
    }
    
    private void initializeExchangeRates() {
        exchangeRates = new HashMap<>();
        exchangeRates.put("USD", 1.0);
        exchangeRates.put("EUR", 0.85);
        exchangeRates.put("GBP", 0.73);
        exchangeRates.put("JPY", 110.0);
        exchangeRates.put("CAD", 1.25);
        exchangeRates.put("AUD", 1.35);
    }
    
    public Double convertCurrency(Double amount, String fromCurrency, String toCurrency) {
        if (exchangeRates.containsKey(fromCurrency) && exchangeRates.containsKey(toCurrency)) {
            Double fromRate = exchangeRates.get(fromCurrency);
            Double toRate = exchangeRates.get(toCurrency);
            return (amount / fromRate) * toRate;
        }
        return null;
    }
    
    public Map<String, Double> getAllExchangeRates() {
        return new HashMap<>(exchangeRates);
    }
    
    public void updateExchangeRate(String currency, Double rate) {
        exchangeRates.put(currency, rate);
    }
    
    public Double getExchangeRate(String currency) {
        return exchangeRates.get(currency);
    }
}
