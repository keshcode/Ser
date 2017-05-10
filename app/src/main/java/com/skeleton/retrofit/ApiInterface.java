package com.skeleton.retrofit;


import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

import static com.skeleton.constant.ApiKeyConstant.AUTHORIZATION;
import static com.skeleton.retrofit.ApiInterface.USER_LOGIN;

/**
 * Developer: Saurabh Verma
 * Dated: 27-09-2016.
 */
public interface ApiInterface {
    String UPDATE_LOCATION = "api/v1/user/updateLocation";
    String USER_SIGNUP = "api/user/register";
    String USER_LOGIN = "api/user/login";

    /**
     * Api Call for user signup
     *
     * @param map : {@link HashMap} of user data
     * @return : response of server {@link Call<CommonResponse>}
     */
    @Multipart
    @POST(USER_SIGNUP)
    Call<com.skeleton.model.Response> userRegister(@PartMap HashMap<String, RequestBody> map);

    /**
     * Api call for user Login
     *
     * @param authorization : header for api call
     * @param map           : login data of user
     * @return : response of server {@link CommonResponse}
     */
    @POST(USER_LOGIN)
    Call<CommonResponse> userLogin(@Header(AUTHORIZATION) String authorization,
                                   @Body HashMap<String, String> map);



//    /**
//     * @param map
//     * @return
//     */
//    @Multipart
//    @POST("api/v1/user/createUser")
//    Call<LoginResponse> register(@PartMap HashMap<String, RequestBody> map);
//
//
//    /**
//     * @param map
//     * @return
//     */
//    @FormUrlEncoded
//    @PUT("api/v1/user/socialLogin")
//    Call<LoginResponse> socialLogin(@FieldMap HashMap<String, String> map);
//
//    /**
//     * @param authorization
//     * @param map
//     * @return
//     */
//    @FormUrlEncoded
//    @PUT("api/v1/user/loginToken")
//    Call<LoginResponse> accessTokenLogin(@Header(AUTHORIZATION) String authorization,
//                                         @FieldMap HashMap<String, String> map);
//
//
//    /**
//     * @param email
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("api/v1/user/forgotpassword")
//    Call<CommonResponse> forgotPassword(@Field("email") String email);
//
//    /**
//     * @param map
//     * @return
//     */
//    @FormUrlEncoded
//    @PUT("api/v1/user/loginCredential")
//    Call<LoginResponse> login(@FieldMap HashMap<String, String> map);


    /**
     * Update location call.
     *
     * @param authorization the authorization
     * @param map           the map
     * @return the call
     */
    @FormUrlEncoded
    @POST(UPDATE_LOCATION)
    Call<CommonParams> updateLocation(@Header(AUTHORIZATION) String authorization,
                                      @FieldMap HashMap<String, String> map);

}

