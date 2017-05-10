package com.skeleton.retrofit;

import android.content.Context;

import com.google.gson.JsonSyntaxException;
import com.skeleton.R;
import com.skeleton.util.Log;
import com.skeleton.util.Util;
import com.skeleton.util.customview.ProgressDialog;
import com.skeleton.util.dialog.CustomAlertDialog;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.skeleton.constant.AppConstant.SESSION_EXPIRED;
import static com.skeleton.retrofit.ErrorUtils.DEFAULT_STATUS_CODE;


/**
 * Developer: Saurabh Verma
 * Dated: 27-09-2016.
 */

/**
 * Custom Retrofit ResponseResolver
 *
 * @param <T> the type parameter
 */
public abstract class ResponseResolver<T> implements Callback<T> {
    public static final String UNEXPECTED_ERROR_OCCURRED = "Something went wrong. Please try again later";
    private static final String NO_INTERNET_MESSAGE = "No internet connection. Please try again later.";
    private static final String REMOTE_SERVER_FAILED_MESSAGE = "Application server could not respond. Please try later.";
    private static final String PARSING_ERROR = "Parsing error";
    private static final String RESOLVE_NETWORK_ERROR = "Resolve Network Error = ";

    private WeakReference<Context> weakContext = null;
    private Boolean showLoading = false;
    private Boolean showError = false;

    /**
     * Instantiates a new Response resolver.
     *
     * @param mContext the Context
     */
    public ResponseResolver(final Context mContext) {
        this.weakContext = new WeakReference<>(mContext);
    }


    /**
     * Instantiates a new Response resolver.
     *
     * @param mContext    the activity
     * @param showLoading the show loading
     */
    public ResponseResolver(final Context mContext, final Boolean showLoading) {
        this.weakContext = new WeakReference<>(mContext);
        this.showLoading = showLoading;
        if (showLoading) {
            ProgressDialog.showProgressDialog(mContext, mContext.getString(R.string.loading));
        }
    }


    /**
     * Instantiates a new Response resolver.
     *
     * @param mContext    the activity
     * @param showLoading the show loading
     * @param showError   the show error
     */
    public ResponseResolver(final Context mContext, final Boolean showLoading, final Boolean showError) {
        this.weakContext = new WeakReference<>(mContext);
        this.showLoading = showLoading;
        this.showError = showError;
        if (showLoading) {
            ProgressDialog.showProgressDialog(mContext, mContext.getString(R.string.loading));
        }

    }

    /**
     * Success.
     *
     * @param t the t
     */
    public abstract void success(T t);

    /**
     * Failure.
     *
     * @param error the error
     */
    public abstract void failure(APIError error);

    @Override
    public void onResponse(final Call<T> t, final Response<T> tResponse) {
        ProgressDialog.dismissProgressDialog();
//        try {
//            Log.e(BuildConfig.APPLICATION_ID,
// String.format("Status Code >>> %s", new JSONObject(new Gson().toJson(tResponse.body())).getInt("statusCode")));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        if (tResponse.isSuccessful()) {
            success(tResponse.body());
        } else {
            fireError(ErrorUtils.parseError(tResponse));
        }
    }

    @Override
    public void onFailure(final Call<T> t, final Throwable throwable) {
        ProgressDialog.dismissProgressDialog();
        fireError(new APIError(DEFAULT_STATUS_CODE, resolveNetworkError(throwable)));
    }


    /**
     * Fire error.
     *
     * @param apiError the api error
     */
    public void fireError(final APIError apiError) {
        if (showError) {
            if (weakContext.get() != null) {
                if (checkAuthorizationError(apiError)) {
                    new CustomAlertDialog.Builder(weakContext.get())
                            .setMessage(apiError.getMessage())
                            .setPositiveButton(R.string.text_ok, new CustomAlertDialog.CustomDialogInterface.OnClickListener() {
                                @Override
                                public void onClick() {

                                }
                            })
                            .show();
                }
            }
        }
        failure(apiError);
    }


    /**
     * Check authorization error boolean.
     *
     * @param apiError the api error
     * @return boolean
     */
    public Boolean checkAuthorizationError(final APIError apiError) {
        if (apiError.getStatusCode() == SESSION_EXPIRED) {
            Util.restartAppOnSessionExpired(weakContext.get());
            return false;
        }
        return true;
    }

    /**
     * Method resolve network errors
     *
     * @param cause Object of Throwable
     * @return message of network error
     */
    private String resolveNetworkError(final Throwable cause) {
        Log.e(RESOLVE_NETWORK_ERROR, String.valueOf(cause.toString()));
        if (cause instanceof UnknownHostException) {
            return NO_INTERNET_MESSAGE;
        } else if (cause instanceof SocketTimeoutException) {
            return REMOTE_SERVER_FAILED_MESSAGE;
        } else if (cause instanceof ConnectException) {
            return NO_INTERNET_MESSAGE;
        } else if (cause instanceof JsonSyntaxException) {
            return PARSING_ERROR;
        }
        return UNEXPECTED_ERROR_OCCURRED;
    }
}
