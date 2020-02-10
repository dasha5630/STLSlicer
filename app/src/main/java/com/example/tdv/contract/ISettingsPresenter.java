package com.example.tdv.contract;

import android.widget.Button;

import java.io.InputStream;

public interface ISettingsPresenter {
    void fileReceived(InputStream in);

    void click();

    void deviceWasConnected(Button btn);

    void stepChanged(Float step, Button btn);

    void timeChanged(Float time, Button btn);
}

