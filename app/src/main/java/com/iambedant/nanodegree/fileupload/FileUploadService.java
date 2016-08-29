package com.iambedant.nanodegree.fileupload;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Kuliza-193 on 3/17/2016.
 */
public interface FileUploadService {
    @Multipart
    @POST("https://api.cloudsightapi.com/image_requests")
    Call<Response> uploadPhoto(@Header("Authorization") String authorisation,@Part("image_request[locale]") RequestBody description,
                                   @Part MultipartBody.Part file);


    @GET("https://api.cloudsightapi.com/image_responses/{token}")
      Call<Response> getImage(@Header("Authorization") String authorisation,@Path("token") String token);


//    @Multipart
//    @POST("http://raven.kuikr.com/upload")
//    Call<JSONObject> uploadPhoto(@Part("image\"; filename=\"image.png\" ") RequestBody file);
}