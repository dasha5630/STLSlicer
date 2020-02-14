package com.example.tdv.presenters;

import android.graphics.Path;

import com.example.tdv.repository.slicer.Point;
import com.example.tdv.contract.IShowSlicePresenter;
import com.example.tdv.contract.IViewSlicerScreen;
import com.example.tdv.repository.slicer.Slicer;

import java.util.ArrayList;

public class ShowSlicePresenter implements IShowSlicePresenter {
    private final String serviceUUID = "0000ffe0-0000-1000-8000-00805f9b34fb";
    private final String characteristicUUID = "00000ffe1-0000-1000-8000-00805f9b34fb";
    private IViewSlicerScreen mView;
    private Slicer slicer;
    private Timer timer;
    private long sliceTime;
    private long preparationTime;
    private long moveCounter;

    public ShowSlicePresenter(IViewSlicerScreen mView){
        this.mView = mView;
        this.slicer = Slicer.getInstance();
        this.preparationTime = 1000;
    }

    @Override
    public void done() {
        mView.writeToService(serviceUUID,characteristicUUID, "M17");
    }

    @Override
    public void timeOut() {
        moveCounter++;
        if(moveCounter != 0 && (moveCounter % 2) == 0){
            slicer.currentZ += slicer.step;
            mView.writeToService(serviceUUID,characteristicUUID, "G1 Z" + slicer.currentZ.toString());
            mView.showSlice(pointsToPath(slicer.slice()));
            timer.setTimeout(sliceTime);
        } else {
            Float emptyStep = slicer.currentZ + 10;
            mView.writeToService(serviceUUID,characteristicUUID, "G1 Z" + emptyStep.toString());
            mView.clearScreen();
            timer.setTimeout(preparationTime);
        }
    }

    @Override
    public void setTime(Float time) {
        this.sliceTime = time.longValue();
        this.timer = Timer.getInstance(this);
        timer.setTimeout(time.longValue());
        timer.sendEmptyMessageDelayed(-1, 100);
    }

    @Override
    public void setStep(Float step) {
        slicer.step = step;
    }

    private ArrayList<Path> pointsToPath(ArrayList<ArrayList<Point>> pointsArray) {
        ArrayList<Path> paths = new ArrayList<>();
        for(ArrayList<Point> points : pointsArray){
            Path path = new Path();
            if (!points.isEmpty()) {
                path.moveTo(points.get(0).getX() * 100, points.get(0).getY() * 100);
                for (Point it : points) {
                    path.lineTo(it.getX() * 100, it.getY() * 100);
                }
                path.close();
            }
            paths.add(path);
        }
        return paths;
    }

}
