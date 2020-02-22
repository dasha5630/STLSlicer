package com.example.tdv;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.tdv.contract.IShowSlicePresenter;
import com.example.tdv.presenters.Timer;

import org.junit.*;
import org.junit.runner.*;

import static org.junit.Assert.*;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public IntentsTestRule<SettingsActivity> mActivity = new IntentsTestRule<SettingsActivity>(SettingsActivity.class) {
        @Override
        protected Intent getActivityIntent() {
         return new Intent().setAction("android.intent.action.MAIN");
        }
    };

   @Before
   public void setUp() throws Exception {

   }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.tdv", appContext.getPackageName());
    }

}
