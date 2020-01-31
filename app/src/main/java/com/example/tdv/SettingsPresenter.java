package com.example.tdv;

import android.graphics.Point;

import com.example.tdv.contract.ISettingsPresenter;
import com.example.tdv.contract.IStartActivity;
import com.example.tdv.contract.IViewSlicerScreen;
import com.example.tdv.repository.slicer.Slicer;

import java.io.InputStream;
import java.util.ArrayList;
/*
TODO make Path from 3DpointArray and return to View
TODO make presenters for each activity
 */
public class SettingsPresenter implements ISettingsPresenter {
    private IStartActivity mStartActivity;
    private Slicer slicer = Slicer.getInstance();


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





}
