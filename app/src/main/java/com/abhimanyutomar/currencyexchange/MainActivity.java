package com.abhimanyutomar.currencyexchange;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    Spinner spinnerBase;
    Spinner spinnerTarget;
    String star,y;
    String finale;
    TextView Target;
    EditText Base;

    //This method
    public void Calculate(View view){
        String base,target,s1,s2,add,amtEntered;
        //Create a new task to download rates from website
        Downloadtask newConversion=new Downloadtask();
        base=spinnerBase.getSelectedItem().toString();//user selected base currency
        target=spinnerTarget.getSelectedItem().toString();// user selected target base currency
        s1="https://free.currencyconverterapi.com/api/v6/convert?q=";
        s2="_";
        star=base+s2+target;
        add=s1+base+s2+target;
        try {
            finale=newConversion.execute(add).get();
            String x;
            JSONObject jsonObject= new JSONObject(finale);
            x=jsonObject.getJSONObject("results").getString(star);
            JSONObject jsonObject2= new JSONObject(x);
            y=jsonObject2.getString("val");
        } catch (Exception e) {
            Log.i("OOPS","1");
            e.printStackTrace();
        }
        amtEntered=Base.getText().toString();
        double amt=Double.valueOf(amtEntered);
        double currentRate=Double.valueOf(y) ;
        double product=amt*currentRate;
        int trunc;
        trunc=(int) product;
        String converted=String.valueOf(trunc);
        Target.setText(converted);
    }

    public class Downloadtask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            URL url;
            String result="";//container

            try {
                //Establish connection using given url
                url=new URL(strings[0]);
                HttpURLConnection Connection;
                Connection=(HttpURLConnection)url.openConnection();
                Connection.connect();
                //Get data and start reading it
                InputStream is=Connection.getInputStream();
                InputStreamReader isReader= new InputStreamReader(is);
                int data=isReader.read();
                //Keep adding to container until end is reached
                while(data!=-1){
                    char current=(char)data;
                    result+=current;
                    data=isReader.read();
                }
            return result;
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("OOPS ",e.toString());
                return "OOPS something went wrong! ";
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinnerBase=(Spinner)findViewById(R.id.spinnerBase);
        spinnerTarget=(Spinner)findViewById(R.id.spinnerTarget);
        Target=(TextView)findViewById(R.id.textViewTarget);
        Base=(EditText)findViewById(R.id.editTextamount);
        Base.setText("1");

        //Creating an arraylist to hold currency symbols
        ArrayList <String> Currencies=new ArrayList<String>();
        Currencies.add("AUD");//Australian Dollar
        Currencies.add("BTC");//Bitcoin
        Currencies.add("CAD");//Canadian dollar
        Currencies.add("CNY");//Chinese Yuan
        Currencies.add("EUR");//Euro
        Currencies.add("GBP");//British Pound
        Currencies.add("INR");//Indian Rupee
        Currencies.add("JPY");//Japanese Yen
        Currencies.add("USD");//United States Dollar
        //New Array adapter so that we can view the contents of the array in the dropdown menu
        ArrayAdapter<String> currencyAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,Currencies);
        currencyAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        //Assigning the adapter to the two spinners
        spinnerBase.setAdapter(currencyAdapter);
        spinnerTarget.setAdapter(currencyAdapter);
    }
}

