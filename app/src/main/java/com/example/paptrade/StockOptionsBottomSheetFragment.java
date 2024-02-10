package com.example.paptrade;

import static android.content.Intent.getIntent;
import static android.content.Intent.getIntentOld;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class StockOptionsBottomSheetFragment  extends BottomSheetDialogFragment {
    private StockItem selectedStock;
    private int position;
    WatchlistManager watchlistManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.popup, container, false);
        watchlistManager = new WatchlistManager(requireContext());
        Button btnBuy = rootView.findViewById(R.id.btnb);
        Button btnSell = rootView.findViewById(R.id.btns);
        TextView tv1=rootView.findViewById(R.id.tv1);
        TextView tv2=rootView.findViewById(R.id.tv2);
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<StockItem> watchlist = watchlistManager.getWatchlist();
                selectedStock=watchlist.get(position);
                watchlistManager.removeStockFromWatchlist(selectedStock);
                Toast.makeText(requireContext(),"Removed From Watchlist",Toast.LENGTH_SHORT).show();
startActivity(new Intent(requireContext(), StockDisplayActivity.class));
                dismiss();

                if (getActivity() instanceof StockDisplayActivity) {
                    ((StockDisplayActivity) getActivity()).updateWatchlist();
                }

            }
        });
tv1.setText(selectedStock.getSymbol() +"   $"+ selectedStock.getPrice());
        btnBuy.setOnClickListener(view -> {
            if (selectedStock != null) {
                Intent buyIntent = new Intent(requireContext(), BuyActivity.class);
                buyIntent.putExtra("symbol", selectedStock.getSymbol());

                buyIntent.putExtra("price", selectedStock.getPrice());
                startActivity(buyIntent);


            }

            dismiss();
        });

        btnSell.setOnClickListener(view -> {

            dismiss();
        });

        return rootView;
    }

    public void setSelectedStock(StockItem selectedStock) {
        this.selectedStock=selectedStock;
    }
public void setPosition(int position){
        this.position=position;
}

}