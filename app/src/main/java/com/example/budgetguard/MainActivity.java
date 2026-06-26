package com.example.budgetguard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button btnAddTransaction, btnViewTransactions, btnBudget, btnReports;
    TextView tvDashboardExpenses, tvDashboardBudget, tvDashboardRemaining;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAddTransaction = findViewById(R.id.btnAddTransaction);
        btnViewTransactions = findViewById(R.id.btnViewTransactions);
        btnBudget = findViewById(R.id.btnBudget);
        btnReports = findViewById(R.id.btnReports);

        tvDashboardExpenses = findViewById(R.id.tvDashboardExpenses);
        tvDashboardBudget = findViewById(R.id.tvDashboardBudget);
        tvDashboardRemaining = findViewById(R.id.tvDashboardRemaining);

        databaseHelper = new DatabaseHelper(this);

        btnAddTransaction.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AddTransactionActivity.class)));

        btnViewTransactions.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, TransactionsActivity.class)));

        btnBudget.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, BudgetActivity.class)));

        btnReports.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ReportsActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();

        String currentMonth = new SimpleDateFormat(
                "yyyy-MM",
                Locale.getDefault()
        ).format(new Date());

        double totalBudget = databaseHelper.getMonthlyBudgetForMonth(currentMonth);
        double totalExpenses = databaseHelper.getTotalExpensesForMonth(currentMonth);
        double remaining = totalBudget - totalExpenses;

        tvDashboardExpenses.setText("Έξοδα τρέχοντος μήνα: " +
                String.format("%.2f", totalExpenses) + " €");

        tvDashboardBudget.setText("Μηνιαίο Budget: " +
                String.format("%.2f", totalBudget) + " €");

        tvDashboardRemaining.setText("Υπόλοιπο μήνα: " +
                String.format("%.2f", remaining) + " €");
    }
}