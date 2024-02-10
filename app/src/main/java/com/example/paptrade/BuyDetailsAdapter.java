package com.example.paptrade;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BuyDetailsAdapter extends RecyclerView.Adapter<BuyDetailsAdapter.ViewHolder> {

    private List<BuyDetails> buyDetailsList;

    public BuyDetailsAdapter(List<BuyDetails> buyDetailsList) {
        this.buyDetailsList = buyDetailsList;
    }
    public void setStockList(List<BuyDetails>buyDetailsList) {
        this.buyDetailsList = buyDetailsList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_buy_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BuyDetails buyDetails = buyDetailsList.get(position);

        // Bind data to the ViewHolder's views
        holder.symbolTextView.setText(buyDetails.getSymbol());
        holder.quantityTextView.setText("Quantity :"+String.valueOf(buyDetails.getQuantity()));
        holder.priceTextView.setText("$"+String.valueOf(buyDetails.getPrice()));
        double change = buyDetails.getChange();
        String changeString= String.format("%.2f", change);
        holder.changetxt.setText("("+changeString+")");
        if (change >= 0) {
            // Positive change, set text color to green
            holder.changetxt.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.grn));
        } else if (change < 0) {
            // Negative change, set text color to red
            holder.changetxt.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.negativeChange));
        }
       // String changeString= String.format("%.2f", change);
       // holder.changetxt.setText("("+changeString+")");
       // holder.changetxt.setText("(" + String.valueOf(buyDetails.getChange())+")");
       // holder.timestampTextView.setText(buyDetails.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return buyDetailsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView symbolTextView,changetxt;

        TextView quantityTextView;
        TextView priceTextView;
      //  TextView timestampTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            symbolTextView = itemView.findViewById(R.id.symbolTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            changetxt=itemView.findViewById(R.id.change);
            //timestampTextView = itemView.findViewById(R.id.timestampTextView);
        }
    }
}

