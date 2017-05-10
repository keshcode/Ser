package com.skeleton.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.skeleton.R;
import com.skeleton.model.Response;

/**
 * Display the response from Server
 */
public class DisplayResponseActivity extends BaseActivity {
    private TextView tvFname, tvLname, tvAccess, tvId, tvDob, tvCreated,
            tvUpdated, tvCountry, tvPhone, tvUserImage, tvGender,
            tvAbout, tvDrinking, tvlanguage;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispaly_response);
        init();
    }

    /**
     * parse data from response and set to textviews
     *
     * @param response : response from server
     */
    private void setData(final Response response) {
        tvFname.setText(response.getData().getUserDetails().getFirstName());
        tvLname.setText(response.getData().getUserDetails().getLastName());
        tvAccess.setText(response.getData().getAccessToken());
        tvId.setText(response.getData().getUserDetails().getId());
        tvDob.setText(response.getData().getUserDetails().getDob());
        tvCreated.setText(response.getData().getUserDetails().getCreatedAt());
        tvUpdated.setText(response.getData().getUserDetails().getUpdatedAt());
        tvCountry.setText(response.getData().getUserDetails().getCountryCode());
        tvPhone.setText(response.getData().getUserDetails().getPhoneNo());
        tvGender.setText(response.getData().getUserDetails().getGender());
        tvAbout.setText(response.getData().getUserDetails().getAboutMe());
        tvlanguage.setText(response.getData().getUserDetails().getLanguage());
    }

    /**
     * initilizes views and variables
     */
    private void init() {
        Response response = (Response) getIntent().getSerializableExtra("response");

        tvFname = (TextView) findViewById(R.id.tvFirstName);
        tvLname = (TextView) findViewById(R.id.tvLastName);
        tvAccess = (TextView) findViewById(R.id.tvAccess);
        tvId = (TextView) findViewById(R.id.tvId);
        tvDob = (TextView) findViewById(R.id.tvDob);
        tvCreated = (TextView) findViewById(R.id.tvCreatedAt);
        tvUpdated = (TextView) findViewById(R.id.tvUpdatedAt);
        tvDob = (TextView) findViewById(R.id.tvDob);
        tvCountry = (TextView) findViewById(R.id.tvCountryCode);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvUserImage = (TextView) findViewById(R.id.tvUserImage);
        tvGender = (TextView) findViewById(R.id.tvGender);
        tvAbout = (TextView) findViewById(R.id.tvAbout);
        tvlanguage = (TextView) findViewById(R.id.tvLanguage);
        setData(response);
    }

}
