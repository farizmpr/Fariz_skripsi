package com.acomp.khobarapp.api;

import com.acomp.khobarapp.model.BearerTokenModel;
import com.acomp.khobarapp.model.UserModel;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetDataService {


    @POST("login")
    Call<BearerTokenModel> getBearerToken(@Body Map registerApiPayload);

    @POST("register")
    Call<JsonObject> registerUserMobile(@Body Map registerApiPayload);

    @POST("user/update")
    Call<JsonObject> updateUser(@Body Map registerApiPayload);

    @POST("user/update/password")
    Call<JsonObject> updatePassword(@Body Map registerApiPayload);

    @GET("user")
    Call<UserModel> getUser();
}
