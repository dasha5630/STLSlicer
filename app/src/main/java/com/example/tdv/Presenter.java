package com.example.tdv;

import com.example.tdv.contract.IPresenter;
import com.example.tdv.repository.slicer.Slicer;

import java.io.InputStream;

public class Presenter implements IPresenter {
    Slicer slicer = new Slicer();

    @Override
    public void fileReceived(InputStream in) {
        slicer.slicing(in);
    }
}
