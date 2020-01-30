package com.example.tdv.repository.ble;

import java.util.List;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

public class BeaconService extends Service implements BluetoothAdapter.LeScanCallback{
    private static final String TAG = BeaconService.class.getSimpleName();

    private BluetoothGatt btGatt;
    private BluetoothAdapter mBluetoothAdapter;

    @Override
    public void onCreate() {
        super.onCreate();
        writeLine("Automate service created...");
        getBTService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        writeLine("Automate service start...");
        if (!isBluetoothSupported()) {
            stopSelf();
        }else{
            if(mBluetoothAdapter!=null && mBluetoothAdapter.isEnabled()){
                startBLEscan();
            }else{
                stopSelf();
            }
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        writeLine("Automate service destroyed...");
        stopBLEscan();
        super.onDestroy();

        if(btGatt!=null){
            btGatt.disconnect();
            btGatt.close();
            btGatt = null;
        }
    }

    @Override
    public boolean stopService(Intent name) {
        writeLine("Automate service stop...");
        stopSelf();
        return super.stopService(name);
    }

    // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
    // BluetoothAdapter through BluetoothManager.
    public BluetoothAdapter getBTService(){
        BluetoothManager btManager = (BluetoothManager) getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = (BluetoothAdapter) btManager.getAdapter();
        return mBluetoothAdapter;
    }

    public boolean isBluetoothSupported() {
        return this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    public void startBLEscan(){
        mBluetoothAdapter.startLeScan(this);
    }

    public void stopBLEscan(){
        mBluetoothAdapter.stopLeScan(this);
    }

    /**
     *
     * @param enable
     */
    public void scanLeDevice(final boolean enable) {
        if (enable) {
            startBLEscan();
        } else {
            stopBLEscan();
        }
    }

    public static void enableDisableBluetooth(boolean enable){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if(enable) {
                bluetoothAdapter.enable();
            }else{
                bluetoothAdapter.disable();
            }
        }
    }

    @Override
    public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
        if(device!=null && device.getName()!=null){
            //Log.d(TAG + " onLeScan: ", "Name: "+device.getName() + "Address: "+device.getAddress()+ "RSSI: "+rssi);
            if(rssi > -90 && rssi <-1){
                writeLine("Automate service BLE device in range: "+ device.getName()+ " "+rssi);
                if (device.getName().equalsIgnoreCase("NCS_Beacon") || device.getName().equalsIgnoreCase("estimote")) {
                    //This Main looper thread is main for connect gatt, don't remove it
                    // Although you need to pass an appropriate context getApplicationContext(),
                    //Here if you use Looper.getMainLooper() it will stop getting callback and give internal exception fail to register //callback
                    new Handler(getApplicationContext().getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            btGatt = device.connectGatt(getApplicationContext(), false, bleGattCallback);
                            Log.e(TAG, "onLeScan btGatt value returning from connectGatt "+btGatt);
                        }
                    });
                }
                stopBLEscan();
            }else{
                //Log.v("Device Scan Activity", device.getAddress()+" "+"BT device is still too far - not connecting");
            }
        }
    }

    BluetoothGattCallback bleGattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            writeLine("Automate service connection state: "+ newState);
            if (newState == android.bluetooth.BluetoothProfile.STATE_CONNECTED){
                writeLine("Automate service connection state: STATE_CONNECTED");
                Log.v("BLEService", "BLE Connected now discover services");
                Log.v("BLEService", "BLE Connected");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        writeLine("Automate service go for discover services");
                        gatt.discoverServices();
                    }
                }).start();
            }else if (newState == android.bluetooth.BluetoothProfile.STATE_DISCONNECTED){
                writeLine("Automate service connection state: STATE_DISCONNECTED");
                Log.v("BLEService", "BLE Disconnected");
            }
        }

        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                writeLine("Automate service discover service: GATT_SUCCESS");
                Log.v("BLEService", "BLE Services onServicesDiscovered");
                //Get service
                List<BluetoothGattService> services = gatt.getServices();
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic,  int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
        }
    };

    private void writeLine(final String message) {

    }

    public boolean writeCharacteristic(byte[] val){
        //check mBluetoothGatt is available
        if (btGatt == null) {
            Log.e(TAG, "lost connection");
            return false;
        }
        BluetoothGattService Service = btGatt.getService(UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e"));
        BluetoothGattCharacteristic charac1 = null;
        boolean status1 = false;

        charac1 = Service.getCharacteristic(UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e"));
        charac1.setValue(val);
        status1 = btGatt.writeCharacteristic(charac1);
        Log.v("________BLESERVICE____", "___WRITE CHARATERISTICS STATUS:_________"+status1);
        if (charac1 == null) {
            Log.e(TAG, "char not found!");
            return false;
        }

        return status1;
    }

}