package com.patialashahi.weatherreport;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView textView2;
    public void find(View view){
        DownloadTask task = new DownloadTask();
        task.execute("https://api.openweathermap.org/data/2.5/weather?q=" + editText.getText().toString() );


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText)findViewById(R.id.editText);
        textView2 = (TextView)findViewById(R.id.textView2);


    }
    public class DownloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection)url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data!= -1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                String msg="";
                JSONObject jsonObject = new JSONObject();
                String weatherInfo = jsonObject.getString("weather");

                JSONArray arr = new JSONArray(weatherInfo);
                for (int i =0;i<arr.length();i++){
                    JSONObject jsonPart = arr.getJSONObject(i);
                    String name="";
                    String description = "";
                    name = jsonPart.getString("main");
                    description = jsonPart.getString("description");

                    if(name != "" && description!=""){
                        msg += name + ":" + description + "\r\n";
                    }

                }
                if(msg != ""){
                    textView2.setText(msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
