package com.example.paptrade;
//BAFI4W8KOY6R5A8D  API KEY
//N10NIF77UEWVDG89 API KEY

import android.content.ActivityNotFoundException;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import java.util.ArrayList;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class StockDisplayActivity extends AppCompatActivity {
    private RecyclerView recyclerViewStocks;
    //////
    private SpeechRecognizer speechRecognizer;
    private WatchlistManager watchlistManager;
    private AutoCompleteTextView autoCompleteTextView;


    private StockAdapter stockAdapter;
    private List<StockItem> stockList;
    private static final String apiKey = "BAFI4W8KOY6R5A8D";

    //"N10NIF77UEWVDG89";
    //  "BAFI4W8KOY6R5A8D";
ImageButton imageButton;
   private static final String[] stockListSymbols = {"TCS","AAPL", "GOOGL", "MSFT","TSLA","INTC", "HPQ","HOG","ABNB","ADBE"};

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_display);
imageButton=findViewById(R.id.micButton);
//        // Initialize SpeechRecognizer
//        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
//        speechRecognizer.setRecognitionListener(new RecognitionListener() {
//            @Override
//            public void onReadyForSpeech(Bundle bundle) {
//
//            }
//
//            @Override
//            public void onBeginningOfSpeech() {
//
//            }
//
//            @Override
//            public void onRmsChanged(float v) {
//
//            }
//
//            @Override
//            public void onBufferReceived(byte[] bytes) {
//
//            }
//
//            @Override
//            public void onEndOfSpeech() {
//
//            }
//
//            @Override
//            public void onError(int i) {
//
//            }
//
//            @Override
//            public void onResults(Bundle results) {
//                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//                if (matches != null && !matches.isEmpty()) {
//                    // Process the recognized speech
//                    String recognizedText = matches.get(0); // Assuming the first match is the most relevant
//                    processVoiceInput(recognizedText);
//                }
//            }
//
//            @Override
//            public void onPartialResults(Bundle bundle) {
//
//            }
//
//            @Override
//            public void onEvent(int i, Bundle bundle) {
//
//            }
//
//            // Implement other methods of RecognitionListener
//        });


       // imageButton.setOnClickListener(v -> startSpeechRecognition());
        watchlistManager = new WatchlistManager(this);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView2);

     recyclerViewStocks = findViewById(R.id.recyclerViewStocks);

        stockList = new ArrayList<>();
        stockAdapter = new StockAdapter(stockList);
        recyclerViewStocks.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewStocks.setAdapter(stockAdapter);
        stockAdapter.setOnItemClickListener(position -> {

            StockItem selectedStock = stockList.get(position);
            watchlistManager.addStockToWatchlist(selectedStock);



        });
        setupAutoCompleteTextView();



        List<StockItem> watchlist = watchlistManager.getWatchlist();


        stockAdapter.setStockList(watchlist);
StockAdapter stockAdapter1=new StockAdapter(watchlist);
recyclerViewStocks.setLayoutManager(new LinearLayoutManager(this));

recyclerViewStocks.setAdapter(stockAdapter1);
        stockAdapter1.setOnItemClickListener(position -> {



            StockItem selectedStock=watchlist.get(position);
           // watchlistManager.removeStockFromWatchlist(selectedStock);

            StockOptionsBottomSheetFragment bottomSheetFragment = new StockOptionsBottomSheetFragment();
//            Intent positionIntent = new Intent(this, StockOptionsBottomSheetFragment.class);
//            positionIntent.putExtra("position", position);

            bottomSheetFragment.setSelectedStock(selectedStock);
            bottomSheetFragment.setPosition(position);
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());


        });


        updateWatchlistRecyclerView2();
    }
    public void updateWatchlist() {
        List<StockItem> updatedWatchlist = watchlistManager.getWatchlist();
        stockAdapter.setStockList(updatedWatchlist);
        stockAdapter.notifyDataSetChanged();
    }

    public void onBackPressed(){

        startActivity(new Intent(StockDisplayActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_right ,  R.anim.slide_left);
finish();

        super.onBackPressed();
    }

    private void setupAutoCompleteTextView() {

        List<String> stockSuggestions = Arrays.asList("JNJ","JPM","PG","FB","META","KO","WMT","MA","GS","CSCO","DIS","BA","AAPL", "GOOGL", "MSFT", "TSLA", "INTC", "HPQ", "HOG", "ABNB", "ADBE", "ADSK");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, stockSuggestions);

        autoCompleteTextView.setAdapter(adapter);


        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedSymbol = (String) parent.getItemAtPosition(position);

            fetchDataFromApi(selectedSymbol);
        });

    }
//    private void fetchDataFromApi(String symbol) {
//
//        // Replace "YOUR_IEX_CLOUD_API_TOKEN" with your actual IEX Cloud API token
//        String apiUrl ="https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol="+symbol+"&apikey=BAFI4W8KOY6R5A8D";
//                //"https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol="+symbol +".BSE&outputsize=full&apikey=BAFI4W8KOY6R5A8D";
//                //"https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + symbol + "&interval=1min&apikey=BAFI4W8KOY6R5A8D" ;
//                //"https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + "BAFI4W8KOY6R5A8D";
//                //"https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=IBM&interval=5min&apikey=BAFI4W8KOY6R5A8D";
//                //"https://cloud.iexapis.com/stable/stock/market/batch?symbols="+symbol+"&types=quote&token=pk_bd21ec04e50f4d3f98932e96faf838d6";
//
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.GET,
//                apiUrl,
//                null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        parseApiResponse(response , symbol);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                        // Handle error
//                    }
//                });
//
//        requestQueue.add(jsonObjectRequest);
//    }
private void fetchDataFromApi(String symbol) {
    String apiUrl = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + apiKey;

    RequestQueue requestQueue = Volley.newRequestQueue(this);

    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.GET,
            apiUrl,
            null,
            response -> parseApiResponse(response, symbol),
            error -> {
                error.printStackTrace();
                Toast.makeText(StockDisplayActivity.this, "Error fetching data from API", Toast.LENGTH_SHORT).show();
            });

    requestQueue.add(jsonObjectRequest);
}
    private void parseApiResponse(JSONObject response , String symbol) {
        try{


            JSONObject stockData = response.getJSONObject("Global Quote");

            String stockSymbol=stockData.getString("01. symbol");
            double stockPrice = stockData.getDouble("05. price");
            double stockChange = stockData.getDouble("09. change"); // Assuming the field is "09. change"


            StockItem stockItem=new StockItem(stockSymbol,stockPrice);
            stockItem.setChange(stockChange);
            watchlistManager.addStockToWatchlist(stockItem);
            Toast.makeText(this,"Added Successfully",Toast.LENGTH_SHORT).show();



            updateWatchlistRecyclerView();
            Log.d("SearchActivity", "parseApiResponse called for symbol: " + symbol);
            Toast.makeText(this,"Added Successfully",Toast.LENGTH_SHORT).show();
            stockAdapter.notifyDataSetChanged();

        } catch(JSONException e) {
            Log.e("parseApiResponseXXXXXXXXXXNNNNNNNNN", "Error parsing JSON data"+response.toString());
            Toast.makeText(this,"Failed ",Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }
    }
    private void updateWatchlistRecyclerView() {
        List<StockItem> watchlist = watchlistManager.getWatchlist();
        for (StockItem stockItem : watchlist) {
            // Fetch the latest data for each stock from the API
            fetchDataForWatchlistStock(stockItem);
        }

        stockAdapter.setStockList(watchlist);
        stockAdapter.notifyDataSetChanged();
    }
    private void updateWatchlistRecyclerView2() {
        List<StockItem> watchlist = watchlistManager.getWatchlist();
        for (StockItem stockItem : watchlist) {
            // Fetch the latest data for each stock from the API
            fetchDataForWatchlistStock(stockItem);
        }


    }

    private void fetchDataForWatchlistStock(StockItem stockItem) {
        String symbol = stockItem.getSymbol();
        String apiUrl = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + apiKey;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                apiUrl,
                null,
                response -> updateStockPrice(response, stockItem),
                error -> {
                    error.printStackTrace();
                    Toast.makeText(StockDisplayActivity.this, "Error fetching data from API", Toast.LENGTH_SHORT).show();
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void updateStockPrice(JSONObject response, StockItem stockItem) {
        try {
            JSONObject stockData = response.getJSONObject("Global Quote");
            double stockPrice = stockData.getDouble("05. price");
            double stockChange = stockData.getDouble("09. change"); // Assuming the field is "09. change"

            // Update the stock price in the watchlist
            stockItem.setPrice(stockPrice);
            stockItem.setChange(stockChange);
           // Toast.makeText(this, "XXXXX", Toast.LENGTH_SHORT).show();

            // Notify the RecyclerView that the data has changed
            stockAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            Log.e("updateStockPrice", "Error parsing JSON data" + response.toString());
           // Toast.makeText(this, "Failed to update stock price", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
//    private void startSpeechRecognition(View view) {
//        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak the stock symbol to add to your watchlist");
//        speechRecognizer.startListening(intent);
//    }

    // Method to process the recognized voice input
    private void processVoiceInput(String input) {
        // Process the recognized text to extract relevant stock symbols
        // For simplicity, let's assume the user mentions the stock symbol directly in the speech
        // You may use NLP techniques for more complex parsing
        String stockSymbol = input.trim().toUpperCase();

        // Fetch data for the recognized stock symbol and add it to the watchlist
        fetchDataFromApi(stockSymbol);
    }

    public void startSpeechRecognition(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        // Specify a different recognition service package name
       // intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "com.example.paptrade");

        // You can also specify the recognition service component name if needed
        // intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, new ComponentName("com.example.customspeechrecognition", "com.example.customspeechrecognition.CustomSpeechRecognitionService"));

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...");

        try {
            startActivityForResult(intent, 1);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Speech recognition not supported on this device",
                    Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (result != null && !result.isEmpty()) {
                    String spokenText = result.get(0);
                    fetchDataFromApi(spokenText);
                    Toast.makeText(this, "You said: " + spokenText, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
//    private void fetchDataFromApi() {
//        // Replace "YOUR_IEX_CLOUD_API_TOKEN" with your actual IEX Cloud API token
//        String apiUrl =  "https://cloud.iexapis.com/stable/stock/market/batch?symbols=TCS,AAPL,GOOGL,MSFT,TSLA,INTC,HPQ,HOG,ABNB,ADBE,ADSK&types=quote&token=pk_bd21ec04e50f4d3f98932e96faf838d6";
//
//                //"https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol="+symbol+".BSE&apikey=BAFI4W8KOY6R5A8D";
//
//              //  "https://cloud.iexapis.com/stable/stock/market/batch?symbols=AAPL,GOOGL,MSFT,TSLA,INTC,HPQ,HOG,ABNB,ADBE,ADSK&types=quote&token=pk_bd21ec04e50f4d3f98932e96faf838d6";
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.GET,
//                apiUrl,
//                null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        parseApiResponse(response);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                        // Handle error
//                    }
//                });
//
//        requestQueue.add(jsonObjectRequest);
//    }
////    private void parseApiResponse(JSONObject response) {
////        try {
////            for (String symbol : stockListSymbols) {
////                if (response.has(symbol)) {
////                    JSONObject stockData = response.getJSONObject(symbol).optJSONObject("quote");
////
////                    if (stockData != null) {
////                        String stockSymbol = stockData.optString("companyName");
////                        double stockPrice = stockData.optDouble("delayedPrice");
////
////                        StockItem stockItem = new StockItem();
////                        stockItem.setSymbol(stockSymbol);
////                        stockItem.setPrice(stockPrice);
////                        stockList.add(stockItem);
////                    } else {
////                        // Handle missing or null stockData for the symbol
////                        Log.e("parseApiResponse", "Missing data for symbol: " + symbol);
////                    }
////                } else {
////                    // Handle missing symbol in the response
////                    Log.e("parseApiResponse", "Symbol not found in response: " + symbol);
////                }
////            }
////
////            stockAdapter.notifyDataSetChanged();
////        } catch (JSONException e) {
////            e.printStackTrace();
////            // Handle JSON parsing error
////        }
////    }
//
//
//    private void parseApiResponse(JSONObject response) {
//        try{
//            for(String symbol : stockListSymbols) {
//                JSONObject stockData = response.getJSONObject(symbol).getJSONObject("quote");
//               String stockSymbol=stockData.getString("companyName");
//                double stockPrice = stockData.getDouble("delayedPrice");
//
//               StockItem stockItem=new StockItem(stockSymbol,stockPrice);
//
//                stockList.add(stockItem);
//            }
//
//            stockAdapter.notifyDataSetChanged();
//
//        } catch(JSONException e) {
//            e.printStackTrace();
//        }
//    }
//


//        private void parseApiResponse(JSONObject response) {
//        try{
//
//                //JSONObject stockData = response.getJSONObject("TCS").getJSONObject("Global Quote");
////                String stockSymbol = stockData.getString("01. symbol");
////                double stockPrice = stockData.getDouble("05. price");
//
//                StockItem stockItem = new StockItem();
//            stockItem.setSymbol(response.getJSONObject("Global Quote").getString("01. symbol"));
//            stockItem.setPrice(response.getJSONObject("Global Quote").getDouble("05. price"));
//                stockList.add(stockItem);
//
//
//            stockAdapter.notifyDataSetChanged();
//
//        } catch(JSONException e) {
//            e.printStackTrace();
//        }
//    }


}