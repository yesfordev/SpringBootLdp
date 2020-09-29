package com.wefunding.ldp.publicdata.construct.license.service;

import com.google.gson.Gson;

/**
 * Created by yes on 2020/09/29
 */
public interface LicenseDongService {

    void saveLicenseDong(Gson gson, String result);

    void saveAllLicenseDongList(Gson gson, int totalCount, String result);

}
