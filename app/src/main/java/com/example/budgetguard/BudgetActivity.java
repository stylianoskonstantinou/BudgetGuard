package com.example.budgetguard;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BudgetActivity extends AppCompatActivity {

    TextView tvCurrentBudget;
    EditText etNewBudget;
    EditText etFoodLimit, etTransportLimit, etBillsLimit, etEntertainmentLimit, etShoppingLimit;
    Button btnSaveBudget, btnSaveCategoryLimits;

    ProgressBar pbFood, pbTransport, pbBills, pbEntertainment, pbShopping;
    TextView tvFoodInfo, tvTransportInfo, tvBillsInfo, tvEntertainmentInfo, tvShoppingInfo;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        tvCurrentBudget = findViewById(R.id.tvCurrentBudget);
        etNewBudget = findViewById(R.id.etNewBudget);

        etFoodLimit = findViewById(R.id.etFoodLimit);
        etTransportLimit = findViewById(R.id.etTransportLimit);
        etBillsLimit = findViewById(R.id.etBillsLimit);
        etEntertainmentLimit = findViewById(R.id.etEntertainmentLimit);
        etShoppingLimit = findViewById(R.id.etShoppingLimit);

        btnSaveBudget = findViewById(R.id.btnSaveBudget);
        btnSaveCategoryLimits = findViewById(R.id.btnSaveCategoryLimits);

        pbFood = findViewById(R.id.pbFood);
        pbTransport = findViewById(R.id.pbTransport);
        pbBills = findViewById(R.id.pbBills);
        pbEntertainment = findViewById(R.id.pbEntertainment);
        pbShopping = findViewById(R.id.pbShopping);

        tvFoodInfo = findViewById(R.id.tvFoodInfo);
        tvTransportInfo = findViewById(R.id.tvTransportInfo);
        tvBillsInfo = findViewById(R.id.tvBillsInfo);
        tvEntertainmentInfo = findViewById(R.id.tvEntertainmentInfo);
        tvShoppingInfo = findViewById(R.id.tvShoppingInfo);

        databaseHelper = new DatabaseHelper(this);

        refreshScreen();

        btnSaveBudget.setOnClickListener(v -> {
            String budgetText = etNewBudget.getText().toString();

            if (budgetText.isEmpty()) {
                Toast.makeText(this, "Δώσε νέο budget", Toast.LENGTH_SHORT).show();
                return;
            }

            double newBudget = Double.parseDouble(budgetText);
            String currentMonth = new SimpleDateFormat(
                    "yyyy-MM",
                    Locale.getDefault()
            ).format(new Date());

            databaseHelper.updateMonthlyBudgetForMonth(currentMonth, newBudget);

            etNewBudget.setText("");
            refreshScreen();

            Toast.makeText(this, "Το budget αποθηκεύτηκε", Toast.LENGTH_SHORT).show();
        });

        btnSaveCategoryLimits.setOnClickListener(v -> {
            updateLimitIfNotEmpty(etFoodLimit, 1);
            updateLimitIfNotEmpty(etTransportLimit, 2);
            updateLimitIfNotEmpty(etBillsLimit, 3);
            updateLimitIfNotEmpty(etEntertainmentLimit, 4);
            updateLimitIfNotEmpty(etShoppingLimit, 5);

            refreshScreen();

            Toast.makeText(this, "Τα όρια κατηγοριών αποθηκεύτηκαν", Toast.LENGTH_SHORT).show();
        });
    }

    private void updateLimitIfNotEmpty(EditText editText, int categoryId) {
        String text = editText.getText().toString();

        if (!text.isEmpty()) {
            double limit = Double.parseDouble(text);
            databaseHelper.updateCategoryLimit(categoryId, limit);
            editText.setText("");
        }
    }

    private void refreshScreen() {
        String currentMonth = new SimpleDateFormat(
                "yyyy-MM",
                Locale.getDefault()
        ).format(new Date());

        double currentBudget = databaseHelper.getMonthlyBudgetForMonth(currentMonth);
        tvCurrentBudget.setText("Τρέχον Budget: " + String.format("%.2f", currentBudget) + " €");

        updateCategoryProgress(1, currentMonth, pbFood, tvFoodInfo);
        updateCategoryProgress(2, currentMonth, pbTransport, tvTransportInfo);
        updateCategoryProgress(3, currentMonth, pbBills, tvBillsInfo);
        updateCategoryProgress(4, currentMonth, pbEntertainment, tvEntertainmentInfo);
        updateCategoryProgress(5, currentMonth, pbShopping, tvShoppingInfo);
    }

    private void updateCategoryProgress(int categoryId,
                                        String month,
                                        ProgressBar progressBar,
                                        TextView infoText) {

        double limit = databaseHelper.getCategoryLimit(categoryId);
        double spent = databaseHelper.getSpentForCategoryForMonth(categoryId, month);
        double remaining = limit - spent;

        int percent = 0;

        if (limit > 0) {
            percent = (int) ((spent / limit) * 100);
        }

        if (percent > 100) {
            percent = 100;
        }

        progressBar.setProgress(percent);

        infoText.setText(
                String.format("%.2f", spent) + " / " +
                        String.format("%.2f", limit) + " €  |  " +
                        "Υπόλοιπο: " + String.format("%.2f", remaining) + " €  |  " +
                        percent + "%"
        );
    }
}