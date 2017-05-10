package com.skeleton.fragment;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skeleton.locationLib.location.LocationConfig;

/**
 * Created by cl-macbookair-71 on 4/21/17.
 */

public class TestLocationFragment extends LocationBaseFragment {

    private static final int TIME_INTERVAL = 1000;
    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        textView = new TextView(getActivity());
        return textView;
    }

    @Override
    public void onLocationUpdate(final Location location) {

        if (textView != null) {
            textView.setText("My Current Location >" + location.getLatitude() + ", " + location.getLongitude());
        }

    }

    @Override
    public LocationConfig setLocationConfig() {

        return new LocationConfig().setRepeated(true).setTimeInterval(TIME_INTERVAL);

    }
}
