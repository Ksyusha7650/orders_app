package com.example.orders;

public class Order {
    public String Number, Date;
    public boolean IsSigned;

    public Order(String number, String date, boolean isSigned) {
        Number = number;
        Date = date;
        IsSigned = isSigned;
    }
}
