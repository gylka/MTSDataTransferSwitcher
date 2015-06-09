package net.gylka.mtsdatatransferswitcher;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCheckState = (Button) findViewById(R.id.btnCheckState);
        btnCheckState.getBackground().setColorFilter(0xFFFFFF00, PorterDuff.Mode.MULTIPLY);
        btnCheckState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ussdStatusCode = "*110*228" + Uri.encode("#");
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussdStatusCode)));
            }
        });

        Button btnSwitchOn = (Button) findViewById(R.id.btnSwitchOn);
        btnSwitchOn.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
        btnSwitchOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if (telephonyManager.getDataState() == TelephonyManager.DATA_DISCONNECTED) {
                    try {
                        setDataConnectivityState(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                String ussdStatusCode = "*110*224" + Uri.encode("#");
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussdStatusCode)));
            }
        });

        Button btnSwitchOff = (Button) findViewById(R.id.btnSwitchOff);
        btnSwitchOff.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
        btnSwitchOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if (telephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED) {
                    try {
                        setDataConnectivityState(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                String ussdStatusCode = "*110*234" + Uri.encode("#");
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussdStatusCode)));
            }
        });

        Button btnBalance = (Button) findViewById(R.id.btnBalance);
        btnBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if (telephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED) {
                    try {
                        setDataConnectivityState(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                String ussdStatusCode = "*110*10" + Uri.encode("#");
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussdStatusCode)));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
        Workaround for changing the state of mobile data connectivity because no other way possible
        to do so for security reasons. Won't work on Android < 2.3 and >= 5.0. Not working on
        MIUI 6 (based on Android 4.3) either.
     */
    private void setDataConnectivityState(boolean state) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Class cmClass = Class.forName(cm.getClass().getName());
        Field iConnectivityManagerField = cmClass.getDeclaredField("mService");
        iConnectivityManagerField.setAccessible(true);
        Object iConnectivityManager = iConnectivityManagerField.get(cm);
        Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
        Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
        setMobileDataEnabledMethod.setAccessible(true);
        setMobileDataEnabledMethod.invoke(iConnectivityManager, state);
    }
}
