package com.example.tekquotez;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tekquotez.services.ApiService;
import com.example.tekquotez.services.ServiceBuilder;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "TEKQUOTEZ";
    ImageButton Next;
    ImageButton Prev;
    TextView display;
    TextView quoteAuthor;
    CardView quoteCard;
    private Call<List<Quote>> quoteCall;
    private ApiService apiService;
    private List<Quote> quotes;
    private int quoteIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Next = findViewById(R.id.imageBtNext);
        Prev = findViewById(R.id.imageBtPrev);
        display = findViewById(R.id.text);
        quoteCard = findViewById(R.id.quoteCard);
        quoteAuthor = findViewById(R.id.author);


        makeApiCall();

        Next.setOnClickListener(view -> {
            populateViews();
            Animatoo.animateZoom(DetailActivity.this);
        });


        Prev.setOnClickListener(view -> {
            prevQuote();
           Animatoo.animateSplit(DetailActivity.this);
        });
    }

    private void makeApiCall(){
        apiService = ServiceBuilder.buildService(ApiService.class);
        quoteCall = apiService.getQuotes();
        quoteCall.enqueue(new Callback<List<Quote>>() {
            @Override
            public void onResponse(Call<List<Quote>> call, Response<List<Quote>> response) {
                quotes = response.body();
                populateViews();
            }

            @Override
            public void onFailure(Call<List<Quote>> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "No Internet Connection. Please connect to the Internet", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void populateViews() {
        String quoteText = quotes.get(quoteIndex).getQuote();
        String author = quotes.get(quoteIndex).getAuthor();
        display.setText(quoteText);
        quoteAuthor.setText(author);
        if(quoteIndex < quotes.size()-1)
        quoteIndex++;
        else{Toast.makeText(this, "This is the last quote", Toast.LENGTH_SHORT).show();}
    }

    private void prevQuote(){
        if(quoteIndex>=2){
            quoteIndex = quoteIndex-2;
            String quoteText = quotes.get(quoteIndex).getQuote();
            String author = quotes.get(quoteIndex).getAuthor();
            display.setText(quoteText);
            quoteAuthor.setText(author);
        }else{Toast.makeText(this, "This is the first quote", Toast.LENGTH_SHORT).show();}
        if(quoteIndex < quotes.size()-1)
        quoteIndex++;
        else{Toast.makeText(this, "This is the last quote", Toast.LENGTH_SHORT).show();}
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}

