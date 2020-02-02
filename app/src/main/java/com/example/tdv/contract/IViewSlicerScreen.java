package com.example.tdv.contract;

import android.graphics.Point;

import java.util.ArrayList;

public interface IViewSlicerScreen {
    void showSlice(ArrayList<Point> points);
    void writeToService(String characteristic, byte[] value);
    void writeToService(String characteristic, String  value);
}
