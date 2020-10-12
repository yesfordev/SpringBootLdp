package com.wefunding.ldp.publicdata.construct.title.service;

import com.google.gson.Gson;

/**
 * Created by yes on 2020/10/06
 */
public interface RegisterTitleService {

    void saveRegistserTitle(Gson gson, String result);

    void saveRegisterTitleList(Gson gson, int totalCount, String result);
}
