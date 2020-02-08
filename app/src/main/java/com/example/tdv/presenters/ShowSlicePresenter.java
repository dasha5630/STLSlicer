package com.example.tdv.presenters;

import android.graphics.Path;

import com.example.tdv.repository.slicer.Point;
import com.example.tdv.contract.IShowSlicePresenter;
import com.example.tdv.contract.IViewSlicerScreen;
import com.example.tdv.repository.slicer.Slicer;

import java.util.ArrayList;

public class ShowSlicePresenter implements IShowSlicePresenter {
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
    }

    @Override
    public void timeOut() {
        moveCounter++;
        if(moveCounter != 0 && (moveCounter % 2) == 0){
            slicer.currentZ += slicer.step;
            mView.writeToService("0000ffe0-0000-1000-8000-00805f9b34fb", slicer.currentZ.toString());
            mView.showSlice(pointsToPath(slicer.slice()));
            timer.setTimeout(sliceTime);
        } else {
            mView.writeToService("0000ffe0-0000-1000-8000-00805f9b34fb", "10");
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

/*    private ArrayList<Point> pointsToPath(ArrayList<com.example.tdv.repository.slicer.Point> points3D){
        if(points3D == null){
            throw new IllegalArgumentException("points3D null");
        }
        ArrayList<Point> points2D = new ArrayList<>();

        for(com.example.tdv.repository.slicer.Point p: points3D){
            points2D.add(new Point(Math.round(p.getX()), Math.round(p.getY())));
        }
        return points2D;
    }*/
}
