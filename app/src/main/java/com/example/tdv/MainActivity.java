package com.example.tdv;

import android.content.*;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;

import java.io.*;
import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.graphics.Point;

import com.example.tdv.contract.IPresenter;
import com.example.tdv.contract.IViewSlicerScreen;


public class MainActivity extends Activity implements IViewSlicerScreen {

    View dv;
    IPresenter presenter;
    ArrayList<Point> points = new ArrayList<>();


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dv = new DrawView(this);
        setContentView(dv);
        presenter = new Presenter(this);

        Intent intent = getIntent();
        String action = intent.getAction();

        if (action.compareTo(Intent.ACTION_VIEW) == 0) {
            String scheme = intent.getScheme();
            ContentResolver resolver = getContentResolver();
            Uri uri = intent.getData();
                try {
                    String name = uri.getLastPathSegment();
                    Log.v("tag", "File intent detected: " + action + " : " + intent.getDataString() + " : " + intent.getType() + " : " + name);
                } catch (NullPointerException e) {
                    Log.e("tag", "File intent uri is null");
                    e.printStackTrace();
                }

                InputStream input = null;
                try {
                    input = resolver.openInputStream(uri);
                    presenter.fileReceived(input);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

        }
    }

    @Override
    public void showSlice(ArrayList<Point> points) {
        this.points.addAll(points);
    }

    class DrawView extends View {

        Paint p;
        Rect rect;
        Path path;


        public DrawView(Context context) {
            super(context);
            p = new Paint();
            rect = new Rect();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawARGB(0xff, 0, 0, 0);
            p.setColor(Color.WHITE); //brush color
            p.setStrokeWidth(10); //brush size

                if(!points.isEmpty()) {
                    for (Point it : points) {
                        canvas.drawPoint(it.x * 100, it.y * 100, p);
                    }
                }
        }

    }
}
