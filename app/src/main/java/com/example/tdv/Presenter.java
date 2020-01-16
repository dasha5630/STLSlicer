package com.example.tdv;

import android.graphics.Point;

import com.example.tdv.contract.IPresenter;
import com.example.tdv.contract.IViewSlicerScreen;
import com.example.tdv.repository.slicer.Slicer;

import java.io.InputStream;
import java.util.ArrayList;

public class Presenter implements IPresenter {
    IViewSlicerScreen mView;
    Slicer slicer;
    ArrayList<Point> points2D;

    public Presenter(IViewSlicerScreen mView){
        this.mView = mView;
        this.slicer = new Slicer();
        this.points2D = new ArrayList<>();
    }

    @Override
    public void fileReceived(InputStream in) {
        points3DToPoints2D(slicer.slicing(in), points2D);
        mView.showSlice(points2D);
    }

    private ArrayList<Point> points3DToPoints2D(ArrayList<com.example.tdv.repository.slicer.Point> points3D, ArrayList<Point> points2D){

        if(points3D == null){
            throw new IllegalArgumentException("points3D null");
        }

        for(com.example.tdv.repository.slicer.Point p: points3D){
            points2D.add(new Point(Math.round(p.getX()), Math.round(p.getY())));
        }

        if(points2D == null){
            throw new IllegalArgumentException("points2D null");
        }
        return points2D;
    }
}
