package com.pandaos.networkexample;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.pandaos.networkexample.utils.NetworkUtil;


public class MainActivity extends ActionBarActivity {

    TextView networkStatusTv;
    TextView networkNameTv;
    TextView networkGatewayTv;

    boolean isWifiAlertEnabled = true; //flag for enabling the wifi connection dialog

    BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int networkState = NetworkUtil.getConnectionStatus(context);

            networkStatusTv.setText(NetworkUtil.getConnectionStatusString(context));
            networkGatewayTv.setText(NetworkUtil.getGateway(context));

            switch (networkState) {
                case NetworkUtil.WIFI:
                    networkNameTv.setText(NetworkUtil.getWifiName(context));
                    break;
                case NetworkUtil.MOBILE:
                    networkNameTv.setText(NetworkUtil.getMobileNetworkName(context));
                    break;
                case NetworkUtil.NOT_CONNECTED:
                    networkNameTv.setText("None");
                    break;
            }

            if (networkState != NetworkUtil.WIFI && isWifiAlertEnabled){ //no wifi connection and alert dialog allowed
                isWifiAlertEnabled = false;// disable alert
                showWifiDialog(context);
            }
            if (networkState == NetworkUtil.WIFI && !isWifiAlertEnabled) // wifi connected and alert disabled
                isWifiAlertEnabled = true; //re-enable alert
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkStatusTv = (TextView) findViewById(R.id.main_activity_network_status_text);
        networkNameTv = (TextView) findViewById(R.id.main_activity_network_name_text);
        networkGatewayTv = (TextView) findViewById(R.id.main_activity_network_gateway_text);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(networkChangeReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(networkChangeReceiver);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_panda_website) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.panda-os.com"));
            startActivity(browserIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void activateWifi() {
        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
        }
    }

    public void showWifiDialog(Context context) {
        AlertDialog wifiDialog = new AlertDialog.Builder(context). //create a dialog
            setTitle("No Wifi Connection").
            setMessage("No Wifi connection detected. What would you like to do?").
            setNeutralButton("Activate Wifi", new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) { //the user clicked yes
                    activateWifi(); //activate the device's Wifi and bring up the Wifi settings screen
                }
            }).setNegativeButton("Settings", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        }).setPositiveButton("Nothing", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setCancelable(false).create();
        wifiDialog.show(); //show the dialog
    }
}
