package com.example.budgetguard;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddTransactionActivity extends AppCompatActivity {

    EditText etAmount;
    EditText etDescription;
    Spinner spinnerCategory;
    Button btnSave;

    DatabaseHelper databaseHelper;

    String[] categories = {
            "Φαγητό",
            "Μεταφορές",
            "Λογαριασμοί",
            "Διασκέδαση",
            "Αγορές"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        etAmount = findViewById(R.id.etAmount);
        etDescription = findViewById(R.id.etDescription);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSave = findViewById(R.id.btnSave);

        databaseHelper = new DatabaseHelper(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        btnSave.setOnClickListener(v -> {

            String amountText = etAmount.getText().toString();
            String description = etDescription.getText().toString();

            if (amountText.isEmpty()) {
                Toast.makeText(this, "Δώσε ποσό", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount = Double.parseDouble(amountText);
            int categoryId = spinnerCategory.getSelectedItemPosition() + 1;

            String currentDate = new SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
            ).format(new Date());

            String currentMonth = new SimpleDateFormat(
                    "yyyy-MM",
                    Locale.getDefault()
            ).format(new Date());

            databaseHelper.addTransaction(
                    categoryId,
                    amount,
                    "Expense",
                    description,
                    currentDate
            );

            double spent = databaseHelper.getSpentForCategoryForMonth(categoryId, currentMonth);
            double limit = databaseHelper.getCategoryLimit(categoryId);

            if (limit > 0 && spent >= 0.8 * limit) {
                Toast.makeText(this,
                        "Προσοχή! Η κατηγορία " + categories[categoryId - 1] +
                                " έχει φτάσει το 80% του ορίου.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,
                        "Αποθηκεύτηκε στην κατηγορία " + categories[categoryId - 1],
                        Toast.LENGTH_SHORT).show();
            }

            finish();
        });
    }
}