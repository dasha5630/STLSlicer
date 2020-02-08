package com.example.tdv.contract;

import android.graphics.Path;
import android.graphics.Point;

import java.util.ArrayList;

public interface IViewSlicerScreen {
    void showSlice(ArrayList<Path> paths);
    void writeToService(String characteristic, byte[] value);
    void writeToService(String characteristic, String  value);
    void clearScreen();
}
