package com.example.tdv.contract;

public interface IShowSlicePresenter {
    void onDestroy();
    void serviceConnected();
    void timeOut();
    void setTime(Float time);
    void setStep(Float step);
}

