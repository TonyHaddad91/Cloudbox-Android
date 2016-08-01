package com.duriana.cloudbox.Request;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by tonyhaddad on 14/07/2016.
 */
public interface DownloadFileRequest {
    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);
}
