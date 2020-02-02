package com.example.tdv.presenters;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.tdv.contract.IShowSlicePresenter;

public class Timer implements Runnable {

    private long time;
    private long timeout;
    private long setTimeoutTime;
    private static Timer instance = null;
    private IShowSlicePresenter iShowSlicePresenter;

    private Timer(IShowSlicePresenter iShowSlicePresenter){
        this.iShowSlicePresenter = iShowSlicePresenter;
    }

    public static Timer getInstance(IShowSlicePresenter iShowSlicePresenter){
        if(instance == null){
            instance = new Timer(iShowSlicePresenter);
        }
        return instance;
    }
    @Override
    public void run() {
        time = System.currentTimeMillis();
        if(timeout != 0 && ((time - setTimeoutTime) >= timeout)){
            iShowSlicePresenter.timeOut();
            setTimeoutTime = System.currentTimeMillis();
        }
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
        setTimeoutTime = System.currentTimeMillis();
    }

    public long getTime() {
        return time;
    }
}