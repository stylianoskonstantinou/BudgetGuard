package com.example.budgetguard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TransactionsActivity extends AppCompatActivity {

    RecyclerView recyclerTransactions;
    DatabaseHelper databaseHelper;
    TransactionAdapter adapter;
    ArrayList<Transaction> transactionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        recyclerTransactions = findViewById(R.id.recyclerTransactions);

        databaseHelper = new DatabaseHelper(this);
        transactionList = databaseHelper.getAllTransactions();

        adapter = new TransactionAdapter(transactionList);

        recyclerTransactions.setLayoutManager(new LinearLayoutManager(this));
        recyclerTransactions.setAdapter(adapter);
    }
}