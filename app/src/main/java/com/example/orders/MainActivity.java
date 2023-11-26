package com.example.orders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static DataBaseWorker dataBaseWorker;
    private RecyclerView recyclerView;
    private CardAdapter cardAdapter;
    public static List<Order> orders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            dataBaseWorker = new DataBaseWorker(this);
            orders = dataBaseWorker.loadData();
            recyclerView = findViewById(R.id.recyclerView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            cardAdapter = new CardAdapter(orders);
            recyclerView.setAdapter(cardAdapter);
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void addOrder(View view) {
        try {
            Intent myIntent = new Intent(view.getContext(), AddOrder.class);
            startActivity(myIntent);
            //orders = dataBaseWorker.loadData();
            cardAdapter.notifyItemInserted(orders.size());

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteOrder(View view) {
        try {
            dataBaseWorker.deleteOrders(true, "");
            orders = dataBaseWorker.loadData();
            cardAdapter.notifyDataSetChanged();
            finish();
            startActivity(getIntent());
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }
}