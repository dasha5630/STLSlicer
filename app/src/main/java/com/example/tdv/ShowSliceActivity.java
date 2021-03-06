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

import com.example.tdv.contract.IShowSlicePresenter;
import com.example.tdv.contract.IViewSlicerScreen;
import com.example.tdv.presenters.ShowSlicePresenter;

public class ShowSliceActivity extends Activity implements IViewSlicerScreen {

    private View dv;
    private IShowSlicePresenter presenter;
    private ArrayList<Point> points = new ArrayList<>();
    private ArrayList<Path> externalPaths;

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private String mDeviceName;
    private String mDeviceAddress;
    private Float step;
    private Float time;
    private BluetoothLeService mBluetoothLeService;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e("ble","Unable to initialize Bluetooth");
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);

            presenter.serviceConnected();
            presenter.setTime(time);
            presenter.setStep(step);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dv = new DrawView(this);
        presenter = new ShowSlicePresenter(this);
        setContentView(dv);
        externalPaths = new ArrayList<Path>();

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        step = intent.getFloatExtra("STEP", 0);
        time = intent.getFloatExtra("TIME", 10000);

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    public void showSlice(ArrayList<Path> paths) {
        externalPaths = paths;
        dv.invalidate();
    }

    @Override
    public void clearScreen(){
        externalPaths.clear();
        dv.invalidate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
        mBluetoothLeService.disconnect();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    @Override
    public void writeToService(String serviceUUID, String characteristicUUID, byte[] value) {
        mBluetoothLeService.writeCharacteristic(serviceUUID, characteristicUUID, value);
    }

    @Override
    public void writeToService(String serviceUUID, String characteristicUUID, String value) {
        mBluetoothLeService.writeCharacteristic(serviceUUID, characteristicUUID, value);
    }

    class DrawView extends View {

        Paint p;
        Rect rect;
        Path path;

        public DrawView(Context context) {
            super(context);
            p = new Paint();
            rect = new Rect();
            path = new Path();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawARGB(0xff, 0, 0, 0);
            path.reset();
            p.setColor(Color.WHITE); //brush color
            p.setStrokeWidth(10); //brush size
            p.setStyle(Paint.Style.FILL);
            for(Path eP : externalPaths){
                path.op(path, eP, Path.Op.XOR);
            }
            canvas.drawPath(path, p);
        }

    }
}
