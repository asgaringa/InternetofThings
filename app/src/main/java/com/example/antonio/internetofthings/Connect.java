package com.example.antonio.internetofthings;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class  Connect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        final Button button  = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ssid = "/*ssid here*/";
                String pass = "/*password here*/";
                WifiConfiguration  conf = new WifiConfiguration();
                conf.SSID = "\""+ ssid + "\"";
                conf.preSharedKey = "\""+ pass +"\"";

                WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
                wifiManager.setWifiEnabled(true);
                int r1 = wifiManager.addNetwork(conf);
                wifiManager.disconnect();
                wifiManager.enableNetwork(r1, true);
                wifiManager.reconnect();
                wifiManager.removeNetwork(r1);
            }
        });
        final Button next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (mWifi.isConnected()) {
                    Intent intent = new Intent(Connect.this, Rooms.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(Connect.this, "Connect first to the Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}
