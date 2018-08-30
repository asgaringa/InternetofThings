package com.example.antonio.internetofthings;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Rooms extends AppCompatActivity {

    ListView rooms;
    String[] number = {"301", "302", "303", "304", "305"};
    InputStream is = null;
    String line = null;
    String result = null;
    String temp = "";
    String[] arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        rooms = (ListView) findViewById(R.id.rooms);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try{
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://192.168.1.8/db.php");
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        }catch (Exception e){
            System.out.println("Exception 1 caught ");
        }

        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            while((line = reader.readLine())!=null)
                sb.append(line+"\n");
            result = sb.toString();
            is.close();
            System.out.println("------Here is the result------");
            System.out.println(result);
        }catch(Exception e){
            System.out.println("Exception 2 caught");
        }
        try{
            JSONArray jArray = new JSONArray(result);
            int count = jArray.length();

            for(int i=0; i<count; i++){
                JSONObject json_data = jArray.getJSONObject(i);
                temp += json_data.getString("id")+":";
            }

            arr = temp.split(":");
            rooms.setAdapter(new ArrayAdapter<String>(Rooms.this, android.R.layout.simple_list_item_1, arr));

            rooms.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    String msg = "You clicked on position number " +position+ " which is " + rooms.getItemAtPosition(position);
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Rooms.this);
                    dialogBuilder.setTitle("Name");
                    dialogBuilder.setMessage(msg);
                    dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String string = rooms.getItemAtPosition(position).toString();
                            Intent intent = new Intent(Rooms.this, Home.class);
                            intent.putExtra("id", string);
                            startActivity(intent);
                        }
                    });
                    AlertDialog dialog = dialogBuilder.create();
                    dialog.show();
                }
            });


        }catch (Exception e){
            System.out.println("Error on JSON file");
        }


    }
}
