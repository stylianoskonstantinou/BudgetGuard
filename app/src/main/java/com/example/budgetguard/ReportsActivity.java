package com.example.budgetguard;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReportsActivity extends AppCompatActivity {

    EditText etReportMonth;
    Button btnLoadReport;

    TextView tvTotalExpenses, tvTotalBudget, tvRemainingBudget, tvCategoryReport;

    DatabaseHelper databaseHelper;

    String[] categoryNames = {
            "Φαγητό",
            "Μεταφορές",
            "Λογαριασμοί",
            "Διασκέδαση",
            "Αγορές"
    };

    String selectedMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        etReportMonth = findViewById(R.id.etReportMonth);
        btnLoadReport = findViewById(R.id.btnLoadReport);

        tvTotalExpenses = findViewById(R.id.tvTotalExpenses);
        tvTotalBudget = findViewById(R.id.tvTotalBudget);
        tvRemainingBudget = findViewById(R.id.tvRemainingBudget);
        tvCategoryReport = findViewById(R.id.tvCategoryReport);

        databaseHelper = new DatabaseHelper(this);

        selectedMonth = new SimpleDateFormat(
                "yyyy-MM",
                Locale.getDefault()
        ).format(new Date());

        etReportMonth.setText(selectedMonth);

        loadReport(selectedMonth);

        btnLoadReport.setOnClickListener(v -> {
            String monthText = etReportMonth.getText().toString().trim();

            if (monthText.isEmpty()) {
                Toast.makeText(this, "Δώσε μήνα σε μορφή 2026-06", Toast.LENGTH_SHORT).show();
                return;
            }

            selectedMonth = monthText;
            loadReport(selectedMonth);
        });
    }

    private void loadReport(String month) {

        double totalBudget = databaseHelper.getMonthlyBudgetForMonth(month);
        double totalExpenses = databaseHelper.getTotalExpensesForMonth(month);
        double remainingBudget = totalBudget - totalExpenses;

        tvTotalExpenses.setText("Έξοδα μήνα " + month + ": " +
                String.format("%.2f", totalExpenses) + " €");

        tvTotalBudget.setText("Μηνιαίο Budget: " +
                String.format("%.2f", totalBudget) + " €");

        tvRemainingBudget.setText("Υπόλοιπο μήνα: " +
                String.format("%.2f", remainingBudget) + " €");

        showCategoryReport(month);
    }

    private void showCategoryReport(String month) {
        StringBuilder builder = new StringBuilder();

        builder.append("Ανάλυση κατηγοριών για τον μήνα: ")
                .append(month)
                .append("\n\n");

        for (int i = 0; i < categoryNames.length; i++) {
            int categoryId = i + 1;

            double limit = databaseHelper.getCategoryLimitForMonth(categoryId, month);
            double spent = databaseHelper.getSpentForCategoryForMonth(categoryId, month);
            double remaining = limit - spent;

            builder.append(categoryNames[i]).append("\n");
            builder.append("Όριο: ").append(String.format("%.2f", limit)).append(" €\n");
            builder.append("Ξοδεύτηκαν: ").append(String.format("%.2f", spent)).append(" €\n");
            builder.append("Υπόλοιπο: ").append(String.format("%.2f", remaining)).append(" €\n\n");
        }

        tvCategoryReport.setText(builder.toString());
    }
}