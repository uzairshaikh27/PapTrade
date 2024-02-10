package com.example.paptrade;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder> {
    private List<StockItem> stockList;
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public void setStockList(List<StockItem> stockList) {
        this.stockList = stockList;
        notifyDataSetChanged();
    }
    public StockAdapter(List<StockItem> stockList)
    {
        this.stockList = stockList;
    }

    @NonNull
    @Override
    public StockAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stock, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockAdapter.ViewHolder holder, int position) {
        StockItem currentItem = stockList.get(position);

        // ... Bind other views ...

        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
        });
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
        });
        StockItem item = stockList.get(position);
        holder.txtSymbol.setText(item.getSymbol());
        holder.txtPrice.setText(String.format(Locale.getDefault(), "$%.2f", item.getPrice()));
        double change = currentItem.getChange();
        String changeString = String.format("%.2f", change);
        holder.changeTextView.setText("("+changeString+")");
        if (change >= 0) {
            // Positive change, set text color to green
            holder.changeTextView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.grn));
        } else if (change < 0) {
            // Negative change, set text color to red
            holder.changeTextView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.negativeChange));
        }
    }

    @Override
    public int getItemCount() {
        return stockList.size();

    }
    public List<StockItem> getStockList() {
        return stockList;
    }
    public StockItem getItem(int position) {
        if (position >= 0 && position < stockList.size()) {
            return stockList.get(position);
        }
        return null;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSymbol, txtPrice,changeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSymbol = itemView.findViewById(R.id.txtSymbol);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            changeTextView = itemView.findViewById(R.id.txtchange);

        }
    }
}
