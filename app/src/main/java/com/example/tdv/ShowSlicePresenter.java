package com.example.tdv;

import android.graphics.Point;

import com.example.tdv.contract.ISettingsPresenter;
import com.example.tdv.contract.IShowSlicePresenter;
import com.example.tdv.contract.IViewSlicerScreen;
import com.example.tdv.repository.slicer.Slicer;

import java.util.ArrayList;

public class ShowSlicePresenter implements IShowSlicePresenter {
    private IViewSlicerScreen mView;
    private Slicer slicer;

    public ShowSlicePresenter(IViewSlicerScreen mView){
        this.mView = mView;
        this.slicer = Slicer.getInstance();
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
