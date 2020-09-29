package com.wefunding.ldp.publicdata.construct.license.service;

import com.google.gson.Gson;

/**
 * Created by yes on 2020/09/29
 */
public interface LicenseBasicService {

    void saveLicenseBasic(Gson gson, String result);

    void saveAllLicenseBasicList(Gson gson, int totalCount, String result);
}
