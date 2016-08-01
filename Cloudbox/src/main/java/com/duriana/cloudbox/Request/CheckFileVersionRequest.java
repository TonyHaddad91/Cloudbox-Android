package com.duriana.cloudbox.Request;

import com.duriana.cloudbox.CloudBoxFileMeta;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by tonyhaddad on 14/07/2016.
 */
public interface CheckFileVersionRequest {
    @GET("{fileName}")
    Call<CloudBoxFileMeta> getVersion(@Path("fileName") String fileName);
}
