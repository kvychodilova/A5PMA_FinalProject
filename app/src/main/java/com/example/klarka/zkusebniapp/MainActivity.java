package com.example.klarka.zkusebniapp;

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
    private Button button;
    private Spinner S1, S2;
    private TextView transferResult;

    private String indexS1;
    private String indexS2;
    private double inputval;

    private String vysledek;

    private String result[] = new String[10];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        S1 = findViewById(R.id.Setter_1);
        S2 = findViewById(R.id.Setter_2);
        button = findViewById(R.id.button);
        editText = findViewById(R.id.editText);
        transferResult = findViewById(R.id.transferResult);

        //Add curency to spinners
       /* List<String> money_list = new ArrayList<>();
        money_list.add("USD");
        money_list.add("EUR");
        money_list.add("GBP");
        */
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.currency, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        S1.setAdapter(adapter);
        S2.setAdapter(adapter);


        S1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                indexS1 = parent.getItemAtPosition(position).toString();
                //String fromValue = parent.getItemAtPosition(position).toString();
                //Toast.makeText(MainActivity.this,"Selected:" + indexS1, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }
        );

        S2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                indexS2 = parent.getItemAtPosition(position).toString();
              //String toValue = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                transferResult.setText("Prekladam..");
                //receive input from  user
                String vstup = editText.getText().toString();
                //transfer to double
                inputval = Double.parseDouble(vstup);
                /*testovani vstupu OK
                Toast.makeText(MainActivity.this,"input:" + inputval, Toast.LENGTH_SHORT).show(); */

                new calculate().execute();
            }
        });

    }

    public class calculate extends AsyncTask<String, String, String []> {

        @Override
        protected void onPreExecute() {
            //pracuje během toho co běží doInBackground
            super.onPreExecute();
        }

        @Override
        protected String [] doInBackground(String... strings) {
            //nasypat do URL data -> z čeho, na co a kolik
            String url;
            try{
                url=getJson("http://free.currencyconverterapi.com/api/v5/convert?q="+indexS1+"_"+indexS2+"&compact="+inputval);
                JSONObject finalValtoObject = new JSONObject(url);
                JSONObject amountarray = finalValtoObject.getJSONObject("results").getJSONObject(indexS1+"_"+indexS2);
                double resultOUT = amountarray.optDouble("val");
                result[0]= String.valueOf(resultOUT);
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
            double vypocet = Double.parseDouble(result[0])*inputval;
            transferResult.setText(Double.toString(vypocet));
        }

        public String getJson(String URL) throws ClientProtocolException, IOException{

                StringBuilder build = new StringBuilder();
                HttpClient client = new DefaultHttpClient();
                HttpGet httpGet =  new HttpGet(URL);
                HttpResponse response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String con;

                while ((con=reader.readLine()) != null){
                    build.append(con);
                }
                return build.toString();

        }


    }
}
