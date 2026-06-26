package com.example.budgetguard;

public class Transaction {

    private int id;
    private int categoryId;
    private double amount;
    private String type;
    private String description;
    private String date;

    public Transaction(int id, int categoryId, double amount,
                       String type, String description, String date) {
        this.id = id;
        this.categoryId = categoryId;
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getCategoryName() {

        switch (categoryId) {

            case 1:
                return "Φαγητό";

            case 2:
                return "Μεταφορές";

            case 3:
                return "Λογαριασμοί";

            case 4:
                return "Διασκέδαση";

            case 5:
                return "Αγορές";

            default:
                return "Άγνωστη";
        }
    }
}