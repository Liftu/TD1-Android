package com.example.td1.View;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.view.View;
import android.widget.Toast;

import com.example.td1.Controller.Constants;
import com.example.td1.Controller.Controller;
import com.example.td1.Model.Breaches;
import com.example.td1.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends Activity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SearchView searchBar;
    private ProgressBar progressBar;

    private Controller controller;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        searchBar = (SearchView) findViewById(R.id.searchViewBar);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onQueryTextChange(String newText) {
                controller.onFilter(newText);
                return true;
            }
        });

        controller = new Controller(this, this.getSharedPreferences(Constants.user_sharedpreferences, MODE_PRIVATE));
        controller.start();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showList(List<Breaches> breachesList) {
        if (breachesList != null) {
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            breachesList.sort(Comparator.comparingInt(Breaches::getPwnCount).reversed());
            mAdapter = new MyAdapter(breachesList, getBaseContext(), getListener());
            recyclerView.setAdapter(mAdapter);
        }
    }


    private MyAdapter.OnItemClickListener getListener() {
        return new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Breaches item) {
                controller.onItemClick(item);
            }
        };
    }


    public void navigateToDetail(String json) {
        Intent breachIntent = new Intent(this, BreachActivity.class);
        breachIntent.putExtra(Constants.current_breach_intent_key, json);
        startActivity(breachIntent);
    }

    public void displayToast(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void showProgressBar()
    {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar()
    {
        progressBar.setVisibility(View.GONE);
    }

}
