package com.example.tdv;

import android.content.Context;
import android.content.Intent;
import android.graphics.Path;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ShowSliceActivityTest {

    @Rule
    public IntentsTestRule<ShowSliceActivity> mActivity = new IntentsTestRule<ShowSliceActivity>(ShowSliceActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            final Intent intent = new Intent(appContext, ShowSliceActivity.class);
            intent.putExtra(ShowSliceActivity.EXTRAS_DEVICE_NAME, "BT05");
            intent.putExtra("STEP", Float.parseFloat("1"));
            intent.putExtra("TIME", Float.parseFloat("2000"));
            intent.putExtra(ShowSliceActivity.EXTRAS_DEVICE_ADDRESS, "00:00:00:00:00:00");
            return intent;
        }
    };

    @Test
    public void showSlice() {
        ArrayList<Path> paths = new ArrayList<>();
        Path path = new Path();
        Path path1 = new Path();
        Path path2 = new Path();
        Path path3 = new Path();

        path1.moveTo(100,100);
        path1.lineTo(200,100);
        path1.lineTo(200,200);
        path1.lineTo(100,200);
        path1.lineTo(100,100);

        path2.moveTo(100,300);
        path2.lineTo(150,300);
        path2.lineTo(150,350);
        path2.lineTo(100,350);
        path2.lineTo(100,300);

        path3.moveTo(125,110);
        path3.lineTo(150,110);
        path3.lineTo(150,150);
        path3.lineTo(125,150);
        path3.lineTo(125,110);

        path.op(path1, path3, Path.Op.XOR);
        path.op(path, path2, Path.Op.XOR);

        paths.add(path);
        paths.add(path1);
        mActivity.getActivity().showSlice(paths);
    }

}