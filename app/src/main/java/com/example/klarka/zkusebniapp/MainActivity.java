package com.example.klarka.zkusebniapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    Spinner S1, S2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        S1 = findViewById(R.id.Setter_1);
        S2 = findViewById(R.id.Setter_2);


        List<String> money_list = new ArrayList<>();
        money_list.add("USD");
        money_list.add("EUR");
        money_list.add("GBP");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,money_list);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        S1.setAdapter(adapter);
        S2.setAdapter(adapter);


        S1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String fromValue = parent.getItemAtPosition(position).toString();
                Toast.makeText(MainActivity.this,"Selected:" + fromValue, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }
        );

        S2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String toValue = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }
}
