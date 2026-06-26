package com.example.budgetguard;

public class Category {
    private int id;
    private String name;
    private double monthlyLimit;

    public Category(int id, String name, double monthlyLimit) {
        this.id = id;
        this.name = name;
        this.monthlyLimit = monthlyLimit;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getMonthlyLimit() { return monthlyLimit; }
}