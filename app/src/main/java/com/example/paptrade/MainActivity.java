package com.example.paptrade;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;

import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String apiKey = "BAFI4W8KOY6R5A8D";
    private static long invested;
private static double  change;
Button  watchlistbtn;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private RecyclerView recyclerView;
    private BuyDetailsAdapter buyDetailsAdapter;
    private List<BuyDetails> buyDetailsList;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        recyclerView = findViewById(R.id.recyclerView);
        buyDetailsList = new ArrayList<>();
        buyDetailsAdapter = new BuyDetailsAdapter(buyDetailsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
recyclerView.setAdapter(buyDetailsAdapter);

fetchTotalBalance();

        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);



        FirebaseAuth auth=FirebaseAuth.getInstance();
        FirebaseUser currentUser=auth.getCurrentUser();

        if (currentUser == null) {
           startActivity(new Intent(this, LoginActivity.class));
            overridePendingTransition(R.anim.slide_right ,  R.anim.slide_left);

            finish();
        }
        else{
            String uid= currentUser.getUid();
            DatabaseReference dbref=FirebaseDatabase.getInstance().getReference().child("user_details").child(uid);
          dbref.addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  TextView userNameTextView = navigationView.getHeaderView(0).findViewById(R.id.userNameTextView);
                  TextView userEmailTextView = navigationView.getHeaderView(0).findViewById(R.id.userEmailTextView);
                  String usrname=snapshot.child("name").getValue(String.class);
                  String emailtxt=snapshot.child("email").getValue(String.class);
                  userNameTextView.setText(usrname);
                  userEmailTextView.setText(emailtxt);
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
          });
//                  addChildEventListener(new ChildEventListener() {
//              @Override
//              public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                  TextView userNameTextView = navigationView.getHeaderView(0).findViewById(R.id.userNameTextView);
//                  TextView userEmailTextView = navigationView.getHeaderView(0).findViewById(R.id.userEmailTextView);
//String usrname=snapshot.child("name").getValue(String.class);
//String emailtxt=snapshot.child("email").getValue(String.class);
//userNameTextView.setText(usrname);
//userEmailTextView.setText(emailtxt);
//              }
//
//              @Override
//              public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//              }
//
//              @Override
//              public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//              }
//
//              @Override
//              public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//              }
//
//              @Override
//              public void onCancelled(@NonNull DatabaseError error) {
//
//              }
//          });
        }


fetchBuyDetails();



//        watchlistbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, StockDisplayActivity.class));
//                overridePendingTransition(R.anim.slide_right ,  R.anim.slide_left);
//
//                finish();
//            }
//        });


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
                        long totalBalance = dataSnapshot.getValue(Long.class);
                        invested=1000000-totalBalance;

                        TextView totalBalanceTextView = findViewById(R.id.totalBal);
                        totalBalanceTextView.setText("$"+String.valueOf(totalBalance));
                        TextView invested_margin=findViewById(R.id.investedmarginval);
                        invested_margin.setText("$"+String.valueOf(invested));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();

            Toast.makeText(MainActivity.this, "Logout Successful", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            overridePendingTransition(R.anim.slide_right ,  R.anim.slide_left);

            finish();

        }
        if(id==R.id.watchlist){
            startActivity(new Intent(MainActivity.this, StockDisplayActivity.class));
            overridePendingTransition(R.anim.slide_right ,  R.anim.slide_left);

            finish();
        }
        return true;
    }

        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchBuyDetails() {

        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("buy_details");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();



        if (currentUser != null) {


            String userId = currentUser.getUid();
           DatabaseReference buyDetailsReference =usersReference.child(userId);


buyDetailsReference.addChildEventListener(new ChildEventListener() {
    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
        double price = dataSnapshot.child("price").getValue(Double.class);
        int quantity = dataSnapshot.child("quantity").getValue(Integer.class);
        String symbol = dataSnapshot.child("symbol").getValue(String.class);
        BuyDetails buyDetails = new BuyDetails(symbol, quantity, price);
         fetchDataFromApi(buyDetails,symbol);
        //buyDetails.setChange(change);
        buyDetailsList.add(buyDetails);
        buyDetailsAdapter.setStockList(buyDetailsList);

                   buyDetailsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});

        } else {
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchDataFromApi(BuyDetails buyDetails,String symbol) {
        String apiUrl = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + apiKey;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                apiUrl,
                null,
                response -> parseApiResponse(response, symbol,buyDetails),
                error -> {
                    error.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error fetching data from API", Toast.LENGTH_SHORT).show();
                });

        requestQueue.add(jsonObjectRequest);

    }
    private void parseApiResponse(JSONObject response , String symbol, BuyDetails buyDetails) {
        try{

            JSONObject stockData = response.getJSONObject("Global Quote");

//            String stockSymbol=stockData.getString("01. symbol");
//            double stockPrice = stockData.getDouble("05. price");
            double  localchange = stockData.getDouble("09. change"); // Assuming the field is "09. change"
buyDetails.setChange(localchange);
            buyDetailsAdapter.notifyDataSetChanged();
           // Toast.makeText(MainActivity.this, "Change is : "+change, Toast.LENGTH_SHORT).show();
//            StockItem stockItem=new StockItem(stockSymbol,stockPrice);
//            stockItem.setChange(stockChange);
          //  watchlistManager.addStockToWatchlist(stockItem);
           // Toast.makeText(this,"Added Successfully",Toast.LENGTH_SHORT).show();



          //  updateWatchlistRecyclerView();
          //  Log.d("SearchActivity", "parseApiResponse called for symbol: " + symbol);
         //   Toast.makeText(this,"Added Successfully",Toast.LENGTH_SHORT).show();
           // stockAdapter.notifyDataSetChanged();

        } catch(JSONException e) {
            Log.e("parseApiResponse", "Error parsing JSON data");
          //  Toast.makeText(this,"Failed ",Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }

    }

}