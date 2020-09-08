package com.example.myapplication;

import android.bluetooth.le.ScanFilter;
import android.companion.AssociationRequest;
import android.companion.BluetoothLeDeviceFilter;
import android.companion.CompanionDeviceManager;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {


    private static final int SELECT_DEVICE_REQUEST_CODE = 42;
    private CompanionDeviceManager deviceManager;
    private AssociationRequest pairingRequest;
    private BluetoothLeDeviceFilter deviceFilter;
    private String TAG = "CompanionDeviceManager";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        deviceManager = getSystemService(CompanionDeviceManager.class);

        ScanFilter.Builder scanFilterBuilder = new ScanFilter.Builder();
        scanFilterBuilder.setServiceUuid(ParcelUuid.fromString("DEVICE-UUID-STRING"));

        deviceFilter = new BluetoothLeDeviceFilter.Builder()
                .setScanFilter(scanFilterBuilder.build()) // This doesn't work
//                .setNamePattern(Pattern.compile("1T")) // This works when setScanFilter is commented
                .build();

        pairingRequest = new AssociationRequest.Builder()
                .addDeviceFilter(deviceFilter)
                .setSingleDevice(true)
                .build();

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                deviceManager.associate(pairingRequest,
                        new CompanionDeviceManager.Callback() {
                            @Override
                            public void onDeviceFound(IntentSender chooserLauncher) {
                                try {
                                    startIntentSenderForResult(chooserLauncher,
                                            SELECT_DEVICE_REQUEST_CODE, null, 0, 0, 0);
                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(CharSequence charSequence) {
                                Log.d(TAG, "Error");
                            }
                        },
                        null);

            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
