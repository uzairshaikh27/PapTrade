package com.example.paptrade;

public class StockItem {
    private String symbol;
    private double price;
    private double change;  // New property for the change in price


    //    public StockItem(String symbol, double price) {
//        this.symbol = symbol;
//        this.price = price;
//    }
//private static final double DOLLAR_TO_INR_RATE = 83.17;
public StockItem(String symbol, double price) {
    this.symbol = symbol;
    this.price = price;
}
    public  StockItem(String symbol, double price, double change){

            this.symbol = symbol;

this.change=change;

            this.price = price;

    }


    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return  price;
    }
    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }
}

