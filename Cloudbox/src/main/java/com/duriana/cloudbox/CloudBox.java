package com.duriana.cloudbox;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.duriana.cloudbox.Request.CheckFileVersionRequest;
import com.duriana.cloudbox.Request.DownloadFileRequest;
import com.lifeofcoding.cacheutlislibrary.CacheUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tonyhaddad on 14/07/2016.
 */
public class CloudBox {
    private final String DEBUG_TAG = "CloudBox";
    private String metaPrefix = "/CloudBoxAPIResourcesMeta/";
    private final String FILE_SYSTEM_PATH = "/files/";
    private static CloudBox instance;
    private String domain = "";
    private boolean logEnabled = true;

    protected CloudBox() {

    }

    public static CloudBox getInstance(Context context, String domain, String metaPrefix) {
        if (instance == null) {
            instance = new CloudBox();
            instance.domain = domain;
            instance.metaPrefix=metaPrefix;
            CacheUtils.configureCache(context);
        }
        return instance;
    }

    public void getFileFromServer(final Context context, final String fileName, final String fileExtensionOnStorage, final OnSyncFinish onSyncFinish) {
        CheckFileVersionRequest fileVersionService = ServiceGenerator.getInstance(domain + metaPrefix).createService(CheckFileVersionRequest.class);
        Call<CloudBoxFileMeta> call = fileVersionService.getVersion(fileName + fileExtensionOnStorage);
        call.enqueue(new Callback<CloudBoxFileMeta>() {
            @Override
            public void onResponse(Call<CloudBoxFileMeta> call, Response<CloudBoxFileMeta> response) {
                if (response.isSuccessful()) {
                    if (logEnabled)
                        Log.d(DEBUG_TAG, "server contacted and has file" + response.body().getUrl());
                    final int serverVersion = response.body().getVersion();
                    final String fileUrl = response.body().getUrl();
                    final String serverMd5 = response.body().getMd5();
                    final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
                    final int currentFileVersion = prefs.getInt(fileName, 0);
                    if (logEnabled)
                        Log.d(DEBUG_TAG, "version" + currentFileVersion);
                    if (serverVersion > currentFileVersion) {
                        DownloadFileRequest downloadService = ServiceGenerator.createService(DownloadFileRequest.class);

                        Call<ResponseBody> downloadCall = downloadService.downloadFile(fileUrl);

                        downloadCall.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    try {
                                        String content = response.body().string();
                                        if (logEnabled)
                                            Log.d(DEBUG_TAG, "File Download Success" + content);
                                        if (response.body().contentLength() > 2) {
                                            Boolean updated = true;
                                            String MD5 = md5(content);
                                            if (!MD5.equals("")) {
                                                if (MD5.equals(serverMd5)) {
                                                    writeToFile(fileName, content);
                                                    prefs.edit().putInt(fileName, serverVersion).commit();

                                                    onSyncFinish.finish(true);
                                                }
                                            } else {
                                                onSyncFinish.finish(false);
                                            }
                                        } else {
                                            onSyncFinish.finish(false);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        onSyncFinish.finish(false);

                                    }
                                } else {
                                    if (logEnabled)
                                        Log.d(DEBUG_TAG, "server contact failed, Code: " + response.code() + "Msg: " + response.errorBody().toString());
                                    response.body().close();
                                    onSyncFinish.finish(false);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                if (logEnabled)
                                    Log.e(DEBUG_TAG, "Error in request" + t.getMessage());
                                onSyncFinish.finish(false);
                            }
                        });

                    }

                } else {
                    if (logEnabled)
                        Log.d(DEBUG_TAG, "server contact failed, Code: " + response.code() + "Msg: " + response.errorBody().toString());
                    onSyncFinish.finish(false);
                }
            }

            @Override
            public void onFailure(Call<CloudBoxFileMeta> call, Throwable t) {
                if (logEnabled)
                    Log.e(DEBUG_TAG, "Error in request" + t.getMessage());
                onSyncFinish.finish(false);


            }
        });
    }


    /**
     * Get the file. If file exists, return file. Otherwise, return null
     */
    public File getFile(final Context context, final String fileName, final String fileExtension) {
        String path = context.getApplicationInfo().dataDir;
        try {
            File file = new File(path + FILE_SYSTEM_PATH + fileName + fileExtension);
            if (file.exists())
                return file;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String readFile(Context context, String filename) {
        String fileContent = CacheUtils.readFile(filename);
        if (fileContent == null || fileContent.equals(""))
            return "";
        else
            return fileContent;
    }


    public String getFileAsStirngFromAssets(final Context context, final String fileName,
                                            final String fileExtension) {

        String jsonString = null;


        // Parse JSON from asset
        if (logEnabled)
            Log.d(DEBUG_TAG, "Parse JSON from asset");

        try {

            // open the inputStream to the file
            InputStream inputStream = context.getAssets().open(
                    fileName + fileExtension);

            int sizeOfJSONFile = inputStream.available();

            // array that will store all the data
            byte[] bytes = new byte[sizeOfJSONFile];

            // reading data into the array from the file
            inputStream.read(bytes);

            // close the input stream
            inputStream.close();

            jsonString = new String(bytes, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Check if file not exists, parse form asset. Otherwise, parse form
        // storage.
        if (getFile(context, fileName, fileExtension) != null) {

            // Parse JSON from storage
            if (logEnabled)
                Log.d(DEBUG_TAG, "Parse JSON from storage");

            String result = "";
            String line;

            try {
                File file = new File(context.getFilesDir(), fileName + fileExtension);

                BufferedReader br = new BufferedReader(new FileReader(file));
                while ((line = br.readLine()) != null) {
                    result += line + "\n";
                }

                if (result != null && result.length() > 2)
                    jsonString = result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (jsonString == null || jsonString.length() < 2)
            jsonString = "{}";

        return jsonString;
    }

    public void writeToFile(final String fileName, final String fileContent) {
        CacheUtils.writeFile(fileName, fileContent);
    }

    public String md5(String input) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(input.getBytes("UTF-8"));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; i++) {
                sb.append(String.format("%02x", array[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return "";
        }

    }

    public void setLogEnabled(boolean logEnabled) {
        this.logEnabled = logEnabled;
    }
}

