package com.example.paptrade;

public class BuyDetails {
    private String userId;
    private String symbol;
    private int quantity;
    private double price;
    private String timestamp;
    private double change;
    private double profitOrLoss;


    // Empty constructor required for Firebase
    public BuyDetails() {
    }

//    public BuyDetails(String userId, String symbol, int quantity, double price, String timestamp) {
public BuyDetails( String symbol, int quantity, double price) {

    //this.userId = userId;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
      //  this.change=change;
      //  this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }
    public double getProfitOrLoss() {
        return profitOrLoss;
    }
    public void setProfitOrLoss(double profitOrLoss) {
        this.profitOrLoss = profitOrLoss;
    }
    public void calculateProfitOrLoss(double currentValue) {
        this.profitOrLoss = (currentValue - getTotalInvestment());
    }

    private double getTotalInvestment() {
        return (quantity * price);
    }
}
