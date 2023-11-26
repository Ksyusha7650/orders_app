package com.example.orders;

public class Order {
    public int ID;
    public String Number, Date;
    public boolean IsSigned;

    public Order(int id, String number, String date, boolean isSigned) {
        ID = id;
        Number = number;
        Date = date;
        IsSigned = isSigned;
    }
}
