package com.example.tdv;

import android.content.Intent;
import android.graphics.Point;

import com.example.tdv.contract.IPresenter;
import com.example.tdv.contract.IStartActivity;
import com.example.tdv.contract.IViewSlicerScreen;
import com.example.tdv.repository.ble.BeaconService;
import com.example.tdv.repository.ble.BluetoothLeService;
import com.example.tdv.repository.ble.DeviceControlActivity;
import com.example.tdv.repository.ble.DeviceScanActivity;
import com.example.tdv.repository.slicer.Slicer;

import java.io.InputStream;
import java.util.ArrayList;
/*
TODO make Path from 3DpointArray and return to View
 */
public class Presenter implements IPresenter {
    private IStartActivity mStartActivity;
    private IViewSlicerScreen mView;
    private Slicer slicer;
    private BeaconService mBeaconService;


    public Presenter(IViewSlicerScreen mView){
        this.mView = mView;
        this.slicer = new Slicer();
    }

    @Override
    public void fileReceived(InputStream in) {
        mView.showSlice(points3DToPoints2D(slicer.slicing(in)));
        mStartActivity.startNewActivity(BeaconService.class);
    }

    private ArrayList<Point> points3DToPoints2D(ArrayList<com.example.tdv.repository.slicer.Point> points3D){
        if(points3D == null){
            throw new IllegalArgumentException("points3D null");
        }
        ArrayList<Point> points2D = new ArrayList<>();

        for(com.example.tdv.repository.slicer.Point p: points3D){
            points2D.add(new Point(Math.round(p.getX()), Math.round(p.getY())));
        }
        return points2D;
    }
}
