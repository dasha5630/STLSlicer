package com.example.tdv.contract;

import java.io.InputStream;

public interface IPresenter {
    void fileReceived(InputStream in);

    void click();

    void done();
}

