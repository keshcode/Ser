package com.skeleton;

import android.app.Application;
import android.content.Context;

import com.skeleton.util.Foreground;
import com.squareup.leakcanary.LeakCanary;

import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Developer: Saurabh Verma
 * Dated: 19-02-2017.
 */

public class MyApplication extends Application {

    private static MyApplication myApplication;

    /**
     * @return instance of MyApplication
     */
    public static MyApplication getApplication() {
        return myApplication;
    }

    /**
     * Getter to access Singleton instance
     *
     * @return instance of MyApplication
     */
    public static Context getAppContext() {
        return getApplication().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(this);
        Foreground.init(this);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Dosis-Medium.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        // Setup singleton instance
        myApplication = this;

        //leak canary
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }
}
