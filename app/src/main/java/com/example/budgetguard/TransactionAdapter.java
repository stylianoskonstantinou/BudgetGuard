package com.example.budgetguard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private ArrayList<Transaction> transactions;

    public TransactionAdapter(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvAmount;
        TextView tvDescription;
        TextView tvCategory;
        TextView tvDate;
        Button btnDeleteTransaction;

        public ViewHolder(View itemView) {
            super(itemView);

            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvDate = itemView.findViewById(R.id.tvDate);
            btnDeleteTransaction = itemView.findViewById(R.id.btnDeleteTransaction);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Transaction transaction = transactions.get(position);

        holder.tvAmount.setText(String.format("%.2f €", transaction.getAmount()));
        holder.tvDescription.setText(transaction.getDescription());
        holder.tvCategory.setText("Κατηγορία: " + transaction.getCategoryName());
        holder.tvDate.setText(transaction.getDate());

        switch (transaction.getCategoryId()) {
            case 1:
                holder.tvCategory.setTextColor(0xFF2E7D32);
                break;
            case 2:
                holder.tvCategory.setTextColor(0xFF1565C0);
                break;
            case 3:
                holder.tvCategory.setTextColor(0xFFC62828);
                break;
            case 4:
                holder.tvCategory.setTextColor(0xFF6A1B9A);
                break;
            case 5:
                holder.tvCategory.setTextColor(0xFFEF6C00);
                break;
            default:
                holder.tvCategory.setTextColor(0xFF000000);
        }

        holder.btnDeleteTransaction.setOnClickListener(v -> {
            DatabaseHelper databaseHelper = new DatabaseHelper(v.getContext());

            databaseHelper.deleteTransaction(transaction.getId());

            int adapterPosition = holder.getAdapterPosition();

            if (adapterPosition != RecyclerView.NO_POSITION) {
                transactions.remove(adapterPosition);
                notifyItemRemoved(adapterPosition);
            }

            Toast.makeText(v.getContext(),
                    "Η συναλλαγή διαγράφηκε",
                    Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }
}