package com.example.tdv.presenters;

import com.example.tdv.ShowSliceActivity;
import com.example.tdv.contract.ISettingsPresenter;
import com.example.tdv.contract.IStartActivity;
import com.example.tdv.repository.slicer.Slicer;

import java.io.InputStream;

/*
TODO make Path from 3DpointArray and return to View
TODO make presenters for each activity
 */
public class SettingsPresenter implements ISettingsPresenter {
    private IStartActivity mStartActivity;
    private Slicer slicer;


    public SettingsPresenter(IStartActivity mStartActivity){
        this.mStartActivity = mStartActivity;
        slicer = Slicer.getInstance();
    }

    @Override
    public void fileReceived(InputStream in) {
        slicer.readFile(in);
    }

    @Override
    public void click() {
        mStartActivity.startNewActivity(ShowSliceActivity.class);
    }

}
