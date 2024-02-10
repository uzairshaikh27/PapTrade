package com.example.paptrade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class BuyActivity extends AppCompatActivity {
    public static int totalBalance = 1000000;
    private String symbol;
    private double price;
    TextView btnSubmit;
    TextView etQuantity, textViewPrice;
    //private int quantity;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        symbol = getIntent().getStringExtra("symbol");
        price = getIntent().getDoubleExtra("price", 0.0);
        FirebaseApp.initializeApp(this);

        TextView textViewSymbol = findViewById(R.id.textViewSymbol);
        textViewPrice = findViewById(R.id.textViewPrice);
       // TextView textViewBalance = findViewById(R.id.textViewBalance);
fetchTotalBalance();

        textViewSymbol.setText(symbol);
        textViewPrice.setText("Price: $" + price);
        //textViewBalance.setText("Available Balance: " + totalBalance);
        btnSubmit = findViewById(R.id.textViewsubmit);
        etQuantity = findViewById(R.id.etquantity);
        //  quantity = Integer.parseInt(etQuantity.getText().toString());
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
//                switch (checkedId) {
//                    case R.id.rbBuy:
//                        // "Buy" RadioButton is selected
//                        break;
//                    case R.id.rbSell:
//                        // "Sell" RadioButton is selected
//                        break;
//                }
//            }
//        });

        //int quantity = Integer.parseInt(etQuantity.getText().toString());

        int selectedRadioButtonId = ((RadioGroup) findViewById(R.id.radioGroup)).getCheckedRadioButtonId();

//        if (selectedRadioButtonId == R.id.rbBuy) {
//            // "Buy" RadioButton is selected, perform buy logic
//
//            btnSubmit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    buyStock();
//                }
//            });
//           // performBuyLogic();
//        } else if (selectedRadioButtonId == R.id.rbSell) {
//            // "Sell" RadioButton is selected, perform sell logic
//            //performSellLogic();
//        }
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyStock();
            }
        });
    }
    private void fetchTotalBalance() {
        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("users");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference userTotalBalanceReference = usersReference.child(userId).child("totalBalance");

            userTotalBalanceReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        int totalBalance2 = dataSnapshot.getValue(Integer.class);
totalBalance=totalBalance2;
                        TextView totalBalanceTextView = findViewById(R.id.textViewBalance);
                        totalBalanceTextView.setText("Available Balance: $"+String.valueOf(totalBalance2));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

    }

    private void buyStock() {
        EditText etQuantity = findViewById(R.id.etquantity);
        int quantity = Integer.parseInt(etQuantity.getText().toString());

        if (quantity <= 0) {
            Toast.makeText(this, "Quantity must be greater than 0", Toast.LENGTH_SHORT).show();
            return;
        }

        double totalPrice = quantity * price; // Calculate the total price

        if (totalPrice > totalBalance) {
            Toast.makeText(this, "Insufficient Balance", Toast.LENGTH_SHORT).show();
            return;
        }

        totalBalance -= totalPrice;
        performBuyLogic();
    }

    //    private void performBuyLogic() {
//        // Assuming you have a Firebase Realtime Database instance
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("buy_details");
//
//        // Create a unique key for each buy transaction
//        String key = databaseReference.push().getKey();
//        int quantity = Integer.parseInt(etQuantity.getText().toString());
//        // Get the current timestamp
//        String timestamp = String.valueOf(System.currentTimeMillis());
//
//        // Create a BuyDetails object
//        BuyDetails buyDetails = new BuyDetails();
//        buyDetails.setSymbol(symbol);
//        buyDetails.setQuantity(quantity);
//        buyDetails.setPrice(price);
//        buyDetails.setTimestamp(timestamp);
//
//        // Store buy details in Firebase under the unique key
//        databaseReference.child(key).setValue(buyDetails);
//
//        // You may also show a success message to the user
//        Toast.makeText(this, "Buy successful!", Toast.LENGTH_SHORT).show();
//    }
    private void performBuyLogic() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("buy_details");

        if (databaseReference == null) {
            Toast.makeText(this, "Error: DatabaseReference is null", Toast.LENGTH_SHORT).show();
            return;
        }



        String userId = user.getUid();
        String key = databaseReference.child(userId).push().getKey();

        if (key == null) {
            Toast.makeText(this, "Error: Generated key is null", Toast.LENGTH_SHORT).show();
            return;
        }

        String quantityStr = etQuantity.getText().toString();

        if (quantityStr.isEmpty()) {
            Toast.makeText(this, "Error: Quantity is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantity = Integer.parseInt(quantityStr);

        if (quantity <= 0) {
            Toast.makeText(this, "Error: Quantity must be greater than 0", Toast.LENGTH_SHORT).show();
            return;
        }



        BuyDetails buyDetails = new BuyDetails();
       // buyDetails.setUserId(userId);
        buyDetails.setSymbol(symbol);
        buyDetails.setQuantity(quantity);
        buyDetails.setPrice(price);
       // buyDetails.setTimestamp(timestamp);

        databaseReference.child(userId).child(key).setValue(buyDetails)
                .addOnSuccessListener(aVoid -> {
                    double totalPrice = quantity * price;
                    totalBalance -= totalPrice;

                    updateTotalBalanceInFirebase(totalBalance, userId);
                    Toast.makeText(this, "Buy successful!", Toast.LENGTH_SHORT).show();

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }

    private void updateTotalBalanceInFirebase(double newBalance, String userid) {
        DatabaseReference balanceReference = FirebaseDatabase.getInstance().getReference("users").child(userid).child("totalBalance");
        balanceReference.setValue(newBalance)
                .addOnSuccessListener(aVoid -> {

                    startActivity(new Intent(this,MainActivity.class));
                    finish();
                    Toast.makeText(this, "Total balance updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error updating total balance: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}