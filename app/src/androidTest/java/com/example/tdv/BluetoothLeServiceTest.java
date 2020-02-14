package com.example.tdv;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;

import static org.junit.Assert.*;

public class BluetoothLeServiceTest {
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    private BluetoothLeService mBluetoothLeService;

    @Test
    public void writeCharacteristic() {
         final ServiceConnection mServiceConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
                if (!mBluetoothLeService.initialize()) {
                    Log.e("ble","Unable to initialize Bluetooth");
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mBluetoothLeService = null;
            }
        };
        Intent gattServiceIntent = new Intent(appContext, BluetoothLeService.class);
        appContext.bindService(gattServiceIntent, mServiceConnection, appContext.BIND_AUTO_CREATE);
        mBluetoothLeService.writeCharacteristic("0000ffe0-0000-1000-8000-00805f9b34fb","00000ffe1-0000-1000-8000-00805f9b34fb", "10");
    }
}