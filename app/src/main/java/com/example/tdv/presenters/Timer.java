package com.example.tdv.presenters;

import android.os.Handler;
import android.os.Message;

import com.example.tdv.contract.IShowSlicePresenter;

public class Timer extends Handler {

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
    public void handleMessage(Message msg) {
        time = System.currentTimeMillis();
        if(timeout != 0 && ((time - setTimeoutTime) >= timeout)){
            iShowSlicePresenter.timeOut();
            setTimeoutTime = System.currentTimeMillis();
        }
        sendEmptyMessageDelayed(-1, 100);
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
        setTimeoutTime = System.currentTimeMillis();
    }

    public long getTime() {
        return time;
    }
}
