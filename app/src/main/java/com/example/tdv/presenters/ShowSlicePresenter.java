package com.example.tdv.presenters;

import android.graphics.Path;

import com.example.tdv.repository.slicer.Point;
import com.example.tdv.contract.IShowSlicePresenter;
import com.example.tdv.contract.IViewSlicerScreen;
import com.example.tdv.repository.slicer.Slicer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class ShowSlicePresenter implements IShowSlicePresenter {
    private final String serviceUUID = "0000ffe0-0000-1000-8000-00805f9b34fb";
    private final String characteristicUUID = "00000ffe1-0000-1000-8000-00805f9b34fb";
    private IViewSlicerScreen mView;
    private Slicer slicer;
    private Timer timer;
    private long sliceTime;
    private long moveCounter;
    private final long preparationTime = 10000;
    private final long  moveToPointTime = 5000;
    private final long  afterStartTime = 1000;

    public ShowSlicePresenter(IViewSlicerScreen mView){
        this.mView = mView;
        this.slicer = Slicer.getInstance();
    }

    @Override
    public void onDestroy() {
        timer.removeCallbacksAndMessages(null);
    }

    @Override
    public void serviceConnected() {
        mView.writeToService(serviceUUID,characteristicUUID, "M17\r\n");
    }

    @Override
    public void timeOut() {
        if(moveCounter == 0){
            mView.writeToService(serviceUUID,characteristicUUID, "G28\r\n");
            timer.setTimeout(preparationTime);
        } else {
            if ((moveCounter % 2) == 0) {
                slicer.currentZ += slicer.step;
                mView.writeToService(serviceUUID, characteristicUUID, "G1 Z" + new BigDecimal(slicer.currentZ.toString()).setScale(2, RoundingMode.UP).doubleValue() + "\r\n");
                mView.showSlice(pointsToPath(slicer.slice()));
                timer.setTimeout(sliceTime + moveToPointTime);
            } else {
                Float emptyStep = slicer.currentZ + 10f;
                mView.writeToService(serviceUUID, characteristicUUID, "G1 Z" + new BigDecimal(emptyStep.toString()).setScale(2, RoundingMode.UP).doubleValue() + "\r\n");
                mView.clearScreen();
                timer.setTimeout(moveToPointTime);
            }
        }
        moveCounter++;
    }

    @Override
    public void setTime(Float time) {
        this.sliceTime = time.longValue();
        this.timer = Timer.getInstance(this);
        timer.setTimeout(afterStartTime);
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
                path.moveTo(points.get(0).getX()*10, points.get(0).getY()*10);
                for (Point it : points) {
                    path.lineTo(it.getX()*10, it.getY()*10);
                }
                path.close();
            }
            paths.add(path);
        }
        return paths;
    }

}
