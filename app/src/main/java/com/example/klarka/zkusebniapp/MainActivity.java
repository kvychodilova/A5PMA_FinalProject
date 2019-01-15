package com.example.klarka.zkusebniapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Console;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private Button button, ButtontoSecondAct;
    private Spinner S1, S2;
    private TextView transferResult;
    private String indexS1,indexS2;
    private double inputval;
    private String result[] = new String[10];
    private int positionS1,positionS2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null) {
            inputval = Double.parseDouble(savedInstanceState.getString("INPUTSET"));
            S1.setSelection(positionS1);
            S2.setSelection(positionS2);
        }
        setContentView(R.layout.activity_main);

        S1 = findViewById(R.id.Setter_1);
        S2 = findViewById(R.id.Setter_2);
        button = findViewById(R.id.button);
        editText = findViewById(R.id.editText);
        transferResult = findViewById(R.id.transferResult);
        ButtontoSecondAct = findViewById(R.id.button2);
        editText.setText("1");


        //Add curency to spinners
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.currency, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        S1.setAdapter(adapter);
        S2.setAdapter(adapter);

        S1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                 @Override
                 public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                     indexS1 = parent.getItemAtPosition(position).toString();
                     positionS1=position;

                 }
                 @Override
                 public void onNothingSelected(AdapterView<?> parent) {}
             }
        );

        S2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                indexS2 = parent.getItemAtPosition(position).toString();
                //String toValue = parent.getItemAtPosition(position).toString();
                positionS2=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transferResult.setText("Wait for..");
                //receive input from  user
                String vstup = editText.getText().toString();
                //transfer to double
                inputval = Double.parseDouble(vstup);
                /*testovani vstupu OK
                Toast.makeText(MainActivity.this,"input:" + inputval, Toast.LENGTH_SHORT).show(); */

                new calculate().execute();
            }
        });

        ButtontoSecondAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });

    }
   @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("INPUTSET", String.valueOf(inputval));
        outState.putInt("Setter1Val",positionS1);
        outState.putInt("Setter2Val",positionS2);
   }

    public void openActivity2() {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

    public class calculate extends AsyncTask<String, String, String[]> {

        @Override
        protected void onPreExecute() {
            //pracuje během toho co běží doInBackground
            super.onPreExecute();
        }

        @Override
        protected String[] doInBackground(String... strings) {
            //nasypat do URL data -> z čeho, na co a kolik
            String url;
            try {
                //Nefunkcni URL
               /* url = getJson("http://free.currencyconverterapi.com/api/v5/convert?q=" + indexS1 + "_" + indexS2 + "&compact=" + inputval);
                JSONObject finalValtoObject = new JSONObject(url);
                JSONObject amountarray = finalValtoObject.getJSONObject("results").getJSONObject(indexS1 + "_" + indexS2);
                double resultOUT = amountarray.optDouble("val");*/

               url=getJson("https://www.amdoren.com/api/currency.php?api_key=piyUHMjLPUvygSTvbFqzbH5mJpAuRT&from="+indexS1+"&to="+indexS2+"&amount="+inputval);
               JSONObject finalValObject = new JSONObject(url);
               //JSONObject amountarray = finalValObject.getJSONObject("amount");
               double resultOUT = finalValObject.optDouble("amount");
                result[0] = String.valueOf(resultOUT);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //return vysledek;
            //System.out.println(vysledek);
            return result;

        }

        @Override
        protected void onPostExecute(String[] strings) {
            //pracuje po doInBackground
            //super.onPostExecute(strings);
            try{
                double vypocet = Double.parseDouble(result[0]);
                transferResult.setText(Double.toString(vypocet));
                transferResult.setTextColor(Color.BLUE);

            }catch(Exception e){
                Toast.makeText(MainActivity.this,"Není přístupné připojení k internetu", Toast.LENGTH_SHORT).show();
            }

        }

        public String getJson(String URL) throws ClientProtocolException, IOException {

            StringBuilder build = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(URL);
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            String con;

            while ((con = reader.readLine()) != null) {
                build.append(con);
            }
            return build.toString();

        }


    }
}
