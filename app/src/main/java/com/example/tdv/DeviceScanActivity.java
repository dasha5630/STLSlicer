/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.tdv;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.tdv.contract.ISettingsPresenter;
import com.example.tdv.contract.IStartActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
public class DeviceScanActivity extends Activity implements View.OnClickListener, IStartActivity {
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    ISettingsPresenter presenter;
    Button next;
    private String mDeviceName = "BT05";
    private String mDeviceAddress;

    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        next = findViewById(R.id.btnRead);
        next.setEnabled(false);
        presenter = new SettingsPresenter(this);

        Intent intent = getIntent();
        String action = intent.getAction();

        if (action.compareTo(Intent.ACTION_VIEW) == 0) {
            String scheme = intent.getScheme();
            ContentResolver resolver = getContentResolver();
            Uri uri = intent.getData();
            try {
                String name = uri.getLastPathSegment();
                Log.v("tag", "File intent detected: " + action + " : " + intent.getDataString() + " : " + intent.getType() + " : " + name);
            } catch (NullPointerException e) {
                Log.e("tag", "File intent uri is null");
                e.printStackTrace();
            }

            InputStream input = null;
            try {
                input = resolver.openInputStream(uri);
                presenter.fileReceived(input);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        mHandler = new Handler();

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            finish();
            return;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
        mBluetoothAdapter.startLeScan(mLeScanCallback);
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(device != null
                                    && device.getAddress() != null
                                    && device.getName() != null
                                    && device.getName().equals(mDeviceName)){
                                next.setEnabled(true);
                                mDeviceAddress = device.getAddress();
                            }
                        }
                    });
                }
            };

    @Override
    public void onClick(View v) {
        presenter.click();
    }

    @Override
    public void startNewActivity(Class o2) {
        final Intent intent = new Intent(this, ShowSliceActivity.class);
        intent.putExtra(ShowSliceActivity.EXTRAS_DEVICE_NAME, mDeviceName);
        intent.putExtra(ShowSliceActivity.EXTRAS_DEVICE_ADDRESS, mDeviceAddress);
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
        startActivity(intent);
    }
}