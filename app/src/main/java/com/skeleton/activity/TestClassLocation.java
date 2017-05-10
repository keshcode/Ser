package com.skeleton.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.skeleton.R;
import com.skeleton.fragment.TestLocationFragment;

/**
 * Created by cl-macbookair-71 on 4/15/17.
 */

public class TestClassLocation extends BaseActivity {

    private TestLocationFragment testLocationFragment;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_test);

        testLocationFragment = (TestLocationFragment) getSupportFragmentManager().findFragmentById(R.id.testLocationFragment);
    }

}
