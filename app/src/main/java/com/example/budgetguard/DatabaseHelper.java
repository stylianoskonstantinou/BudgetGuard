package com.example.budgetguard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "budgetguard.db";
    private static final int DATABASE_VERSION = 4;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE categories (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "monthly_limit REAL NOT NULL)");

        db.execSQL("CREATE TABLE transactions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "category_id INTEGER, " +
                "amount REAL NOT NULL, " +
                "type TEXT NOT NULL, " +
                "description TEXT, " +
                "date TEXT NOT NULL)");

        db.execSQL("CREATE TABLE monthly_budgets (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "month TEXT NOT NULL UNIQUE, " +
                "budget REAL NOT NULL)");

        db.execSQL("CREATE TABLE monthly_category_limits (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "month TEXT NOT NULL, " +
                "category_id INTEGER NOT NULL, " +
                "monthly_limit REAL NOT NULL, " +
                "UNIQUE(month, category_id))");

        db.execSQL("INSERT INTO categories (name, monthly_limit) VALUES ('Φαγητό', 250)");
        db.execSQL("INSERT INTO categories (name, monthly_limit) VALUES ('Μεταφορές', 100)");
        db.execSQL("INSERT INTO categories (name, monthly_limit) VALUES ('Λογαριασμοί', 300)");
        db.execSQL("INSERT INTO categories (name, monthly_limit) VALUES ('Διασκέδαση', 150)");
        db.execSQL("INSERT INTO categories (name, monthly_limit) VALUES ('Αγορές', 200)");
    }

    public void addTransaction(int categoryId, double amount, String type, String description, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("category_id", categoryId);
        values.put("amount", amount);
        values.put("type", type);
        values.put("description", description);
        values.put("date", date);

        db.insert("transactions", null, values);
        db.close();
    }

    public ArrayList<Transaction> getAllTransactions() {
        ArrayList<Transaction> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM transactions ORDER BY id DESC", null);

        if (cursor.moveToFirst()) {
            do {
                list.add(new Transaction(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getDouble(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    public double getMonthlyBudgetForMonth(String month) {
        double budget = 1000;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT budget FROM monthly_budgets WHERE month = ?",
                new String[]{month}
        );

        if (cursor.moveToFirst() && !cursor.isNull(0)) {
            budget = cursor.getDouble(0);
        }

        cursor.close();
        db.close();

        return budget;
    }

    public void updateMonthlyBudgetForMonth(String month, double budget) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("month", month);
        values.put("budget", budget);

        db.insertWithOnConflict(
                "monthly_budgets",
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );

        db.close();
    }

    public void updateCategoryLimit(int categoryId, double limit) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("monthly_limit", limit);

        db.update("categories", values, "id = ?", new String[]{String.valueOf(categoryId)});

        String currentMonth = new SimpleDateFormat(
                "yyyy-MM",
                Locale.getDefault()
        ).format(new Date());

        ContentValues monthlyValues = new ContentValues();
        monthlyValues.put("month", currentMonth);
        monthlyValues.put("category_id", categoryId);
        monthlyValues.put("monthly_limit", limit);

        db.insertWithOnConflict(
                "monthly_category_limits",
                null,
                monthlyValues,
                SQLiteDatabase.CONFLICT_REPLACE
        );

        db.close();
    }

    public double getCategoryLimit(int categoryId) {
        double limit = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT monthly_limit FROM categories WHERE id = ?",
                new String[]{String.valueOf(categoryId)}
        );

        if (cursor.moveToFirst() && !cursor.isNull(0)) {
            limit = cursor.getDouble(0);
        }

        cursor.close();
        db.close();

        return limit;
    }

    public double getCategoryLimitForMonth(int categoryId, String month) {
        double limit = getCategoryLimit(categoryId);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT monthly_limit FROM monthly_category_limits WHERE category_id = ? AND month = ?",
                new String[]{String.valueOf(categoryId), month}
        );

        if (cursor.moveToFirst() && !cursor.isNull(0)) {
            limit = cursor.getDouble(0);
        }

        cursor.close();
        db.close();

        return limit;
    }

    public double getTotalExpensesForMonth(String monthPrefix) {
        double total = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT SUM(amount) FROM transactions WHERE type = 'Expense' AND date LIKE ?",
                new String[]{monthPrefix + "%"}
        );

        if (cursor.moveToFirst() && !cursor.isNull(0)) {
            total = cursor.getDouble(0);
        }

        cursor.close();
        db.close();

        return total;
    }

    public double getSpentForCategoryForMonth(int categoryId, String monthPrefix) {
        double total = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT SUM(amount) FROM transactions WHERE category_id = ? AND type = 'Expense' AND date LIKE ?",
                new String[]{String.valueOf(categoryId), monthPrefix + "%"}
        );

        if (cursor.moveToFirst() && !cursor.isNull(0)) {
            total = cursor.getDouble(0);
        }

        cursor.close();
        db.close();

        return total;
    }

    public void deleteTransaction(int transactionId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(
                "transactions",
                "id = ?",
                new String[]{String.valueOf(transactionId)}
        );

        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS transactions");
        db.execSQL("DROP TABLE IF EXISTS categories");
        db.execSQL("DROP TABLE IF EXISTS monthly_budgets");
        db.execSQL("DROP TABLE IF EXISTS monthly_category_limits");
        onCreate(db);
    }
}