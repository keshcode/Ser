package com.skeleton.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.skeleton.R;
import com.skeleton.fragment.SignInFragment;
import com.skeleton.fragment.SignUpFragment;

/**
 * sign up and sign in
 */
public class ServerCall extends BaseActivity {
    private ViewPager vpDisplayStart;

    /**
     * intilize all variables
     */
    private void init() {
        vpDisplayStart = (ViewPager) findViewById(R.id.vpStart);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_call);
        init();
        FragmentStatePagerAdapter mStatePagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(final int position) {
                if (position == 0) {
                    SignInFragment mSignInFragment = new SignInFragment();
                    return mSignInFragment;
                }
                SignUpFragment mSignUpFragment = new SignUpFragment();
                return mSignUpFragment;
            }

            @Override
            public int getCount() {
                return 2;
            }
        };

        vpDisplayStart.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {

            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });
        vpDisplayStart.setAdapter(mStatePagerAdapter);
    }




}
