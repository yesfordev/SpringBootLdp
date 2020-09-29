package com.wefunding.ldp.publicdata.construct.license.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wefunding.ldp.publicdata.construct.license.dto.licensedongdto.Item;
import com.wefunding.ldp.publicdata.construct.license.dto.licensedongdto.LicenseDongRes;
import com.wefunding.ldp.publicdata.construct.license.entity.LicenseDongEntity;
import com.wefunding.ldp.publicdata.construct.license.mapper.LicenseDongEntityMapper;
import com.wefunding.ldp.publicdata.construct.license.repository.LicenseDongEntityRepository;
import com.wefunding.ldp.publicdata.construct.license.service.LicenseDongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yes on 2020/09/29
 */

@Service
public class LicenseDongServiceImpl implements LicenseDongService {

    private final LicenseDongEntityMapper licenseDongEntityMapper;

    private final LicenseDongEntityRepository licenseDongEntityRepository;

    @Autowired
    public LicenseDongServiceImpl(LicenseDongEntityMapper licenseDongEntityMapper, LicenseDongEntityRepository licenseDongEntityRepository) {
        this.licenseDongEntityMapper = licenseDongEntityMapper;
        this.licenseDongEntityRepository = licenseDongEntityRepository;
    }

    @Override
    public void saveLicenseDong(Gson gson, String result) {
        int start = result.indexOf("item\":");
        int end = result.lastIndexOf("},\"numOfRows");

        String itemString = result.substring(start + "item\":".length(), end);

        Item itemTemp = gson.fromJson(itemString, Item.class);

        LicenseDongEntity licenseDongEntityTemp = licenseDongEntityMapper.toLicenseDongEntity(itemTemp);
        licenseDongEntityRepository.save(licenseDongEntityTemp);

        System.out.println("item save, rnum: " + licenseDongEntityTemp.getRnum() + ", pageNo: 1");
    }

    @Override
    public void saveAllLicenseDongList(Gson gson, int totalCount, String result) {
        LicenseDongRes licenseDongRes;
        licenseDongRes = gson.fromJson(result, new TypeToken<LicenseDongRes>() {
        }.getType());

        List<Item> itemList = licenseDongRes.getResponse().getBody().getItems().getItem();

        /**
         * saveAll 이용
         */
        List<LicenseDongEntity> licenseDongEntityListTemp = licenseDongEntityMapper.toLicenseDongEntityList(itemList);
        System.out.println(licenseDongEntityListTemp);
        licenseDongEntityRepository.saveAll(licenseDongEntityListTemp);
        System.out.println("itemList saveAll, pageNo: " + licenseDongRes.getResponse().getBody().getPageNo() + ", totalCount: " + totalCount);
    }
}
