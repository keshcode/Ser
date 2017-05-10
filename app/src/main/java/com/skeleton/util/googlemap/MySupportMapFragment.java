package com.skeleton.util.googlemap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Developer: Saurabh Verma
 * Dated: 03-03-2017.
 */

public class MySupportMapFragment extends SupportMapFragment {
    private View mOriginalContentView;
    private TouchableWrapper mTouchView;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup parent, final Bundle savedInstanceState) {
        mOriginalContentView = super.onCreateView(inflater, parent, savedInstanceState);
        mTouchView = new TouchableWrapper(getContext());
        mTouchView.addView(mOriginalContentView);
        return mTouchView;
    }

    @Override
    public View getView() {
        return mOriginalContentView;
    }

    /**
     * This method call on map ready
     *
     * @param map instance of map
     */
    public void setMap(final GoogleMap map) {
        mTouchView.initMap(map);
    }

}
