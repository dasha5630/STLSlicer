package com.example.tdv;

import android.content.*;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;

import java.util.ArrayList;

import android.app.Activity;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.graphics.Point;

import com.example.tdv.contract.IPresenter;
import com.example.tdv.contract.IViewSlicerScreen;

public class ShowSliceActivity extends Activity implements IViewSlicerScreen {

    View dv;
    IPresenter presenter;
    ArrayList<Point> points = new ArrayList<>();

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private String mDeviceName;
    private String mDeviceAddress;
    private BluetoothLeService mBluetoothLeService;

    private boolean mConnected = false;


    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e("ble","Unable to initialize Bluetooth");
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {

            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
            }
        }
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dv = new DrawView(this);
        presenter = new SettingsPresenter(this);
        setContentView(dv);

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.done();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d("ble", "Connect request result=" + result);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    @Override
    public void showSlice(ArrayList<Point> points) {
        this.points.addAll(points);
    }


    public Context getContext(){
        return this.getApplicationContext();
    }
    class DrawView extends View {

        Paint p;
        Rect rect;
        Path path;

        Paint p1;
        public DrawView(Context context) {
            super(context);
            p = new Paint();
            p1 = new Paint();
            rect = new Rect();
            path = new Path();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawARGB(0xff, 0, 0, 0);

            p1.setColor(Color.WHITE);
            p.setColor(Color.GREEN); //brush color
            p.setStrokeWidth(10); //brush size
            p.setStyle(Paint.Style.FILL);

                if(!points.isEmpty()) {
                    path.moveTo(points.get(0).x*100,points.get(0).y*100);
                    for (Point it : points) {
                        path.lineTo(it.x*100,it.y*100);
                        canvas.drawPoint(it.x * 100, it.y * 100, p);
                    }
                    path.close();

                    canvas.drawPath(path, p1);
                }
        }

    }
}
