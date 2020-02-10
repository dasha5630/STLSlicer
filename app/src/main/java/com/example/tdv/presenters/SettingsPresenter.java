package com.example.tdv.presenters;

import android.widget.Button;

import com.example.tdv.ShowSliceActivity;
import com.example.tdv.contract.ISettingsPresenter;
import com.example.tdv.contract.IStartActivity;
import com.example.tdv.repository.slicer.STLParser;

import java.io.InputStream;

/*
TODO make Path from 3DpointArray and return to View
TODO make presenters for each activity
 */
public class SettingsPresenter implements ISettingsPresenter {
    private IStartActivity mStartActivity;
    boolean deviceWasConnected = false;
    boolean stepCorrect = false;
    boolean timeCorrect = false;

    public SettingsPresenter(IStartActivity mStartActivity){
        this.mStartActivity = mStartActivity;
    }

    @Override
    public void fileReceived(InputStream in) {
        STLParser.readFile(in);
    }

    @Override
    public void click() {
        mStartActivity.startNewActivity(ShowSliceActivity.class);
    }

    @Override
    public void deviceWasConnected(Button btn) {
        deviceWasConnected = true;
        if(stepCorrect && timeCorrect) btn.setEnabled(true);
        else                           btn.setEnabled(false);

    }

    @Override
    public void stepChanged(Float step, Button btn) {
        if(step > 0) stepCorrect = true;
        else stepCorrect = false;
        if(deviceWasConnected && stepCorrect && timeCorrect) btn.setEnabled(true);
        else                                                 btn.setEnabled(false);
    }

    @Override
    public void timeChanged(Float time, Button btn) {
        if(time > 1000) timeCorrect = true;
        else timeCorrect = false;
        if(deviceWasConnected && stepCorrect && timeCorrect) btn.setEnabled(true);
        else                                                 btn.setEnabled(false);
    }

}
