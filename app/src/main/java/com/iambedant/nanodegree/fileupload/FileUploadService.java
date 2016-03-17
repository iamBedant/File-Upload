package com.iambedant.nanodegree.fileupload;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Kuliza-193 on 3/17/2016.
 */
public interface FileUploadService {
//    @Multipart
//    @POST("upload")
//    Call<ResponseBody> uploadPhoto(@Part("description") RequestBody description,
//                              @Part MultipartBody.Part file);

        @Multipart
    @POST("http://your-domain.com/upload")
        Call<ResponseBody> uploadPhoto(@Part MultipartBody.Part file);


//    @Multipart
//    @POST("http://raven.kuikr.com/upload")
//    Call<JSONObject> uploadPhoto(@Part("image\"; filename=\"image.png\" ") RequestBody file);
}