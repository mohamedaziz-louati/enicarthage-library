package com.enicarthage.library.unused.services;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

@Service
public class StockMarketService {
    
    private Random random;
    
    public StockMarketService() {
        this.random = new Random();
        // Stock market monitoring service for library investment portfolio
    }
    
    public List<String> getStockPrices() {
        List<String> stocks = new ArrayList<>();
        stocks.add("AAPL: $" + (150 + random.nextDouble() * 50));
        stocks.add("GOOGL: $" + (2500 + random.nextDouble() * 500));
        stocks.add("MSFT: $" + (300 + random.nextDouble() * 100));
        stocks.add("AMZN: $" + (3200 + random.nextDouble() * 800));
        stocks.add("TSLA: $" + (800 + random.nextDouble() * 400));
        return stocks;
    }
    
    public Double getStockPrice(String symbol) {
        return 100 + random.nextDouble() * 1000;
    }
    
    public String getMarketStatus() {
        return random.nextBoolean() ? "Bull Market" : "Bear Market";
    }
    
    public List<String> getTopGainers() {
        List<String> gainers = new ArrayList<>();
        gainers.add("NVDA: +" + (random.nextDouble() * 10) + "%");
        gainers.add("AMD: +" + (random.nextDouble() * 8) + "%");
        gainers.add("INTC: +" + (random.nextDouble() * 6) + "%");
        return gainers;
    }
    
    public List<String> getTopLosers() {
        List<String> losers = new ArrayList<>();
        losers.add("META: -" + (random.nextDouble() * 5) + "%");
        losers.add("NFLX: -" + (random.nextDouble() * 4) + "%");
        losers.add("DIS: -" + (random.nextDouble() * 3) + "%");
        return losers;
    }
}
