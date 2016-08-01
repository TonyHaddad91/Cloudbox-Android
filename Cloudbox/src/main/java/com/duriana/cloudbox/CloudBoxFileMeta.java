package com.duriana.cloudbox;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by tonyhaddad on 14/07/2016.
 */
public class CloudBoxFileMeta {

    @JsonProperty("v")
    private int version;

    @JsonProperty("url")
    private String url;

    @JsonProperty("md5")
    private String md5;


    public int getVersion() {
        return version;
    }

    public String getUrl() {
        return url;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
