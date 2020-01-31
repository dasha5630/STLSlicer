package com.example.tdv;

import android.graphics.Point;

import com.example.tdv.contract.IPresenter;
import com.example.tdv.contract.IStartActivity;
import com.example.tdv.contract.IViewSlicerScreen;
import com.example.tdv.repository.slicer.Slicer;

import java.io.InputStream;
import java.util.ArrayList;
/*
TODO make Path from 3DpointArray and return to View
TODO make presenters for each activity
 */
public class SettingsPresenter implements IPresenter {
    private IStartActivity mStartActivity;
    private IViewSlicerScreen mView;
    private Slicer slicer = Slicer.getInstance();

    public SettingsPresenter(IViewSlicerScreen mView){
        this.mView = mView;
    }

    public SettingsPresenter(IStartActivity mStartActivity){
        this.mStartActivity = mStartActivity;
    }

    @Override
    public void fileReceived(InputStream in) {
        slicer.slicing(in);
    }

    @Override
    public void click() {
        mStartActivity.startNewActivity(ShowSliceActivity.class);
    }

    @Override
    public void done() {
        mView.showSlice(points3DToPoints2D(slicer.getPoints()));
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
