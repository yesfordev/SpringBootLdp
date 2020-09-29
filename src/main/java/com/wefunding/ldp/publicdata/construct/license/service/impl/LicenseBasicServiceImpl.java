package com.wefunding.ldp.publicdata.construct.license.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wefunding.ldp.publicdata.construct.license.dto.licensebasicdto.Item;
import com.wefunding.ldp.publicdata.construct.license.dto.licensebasicdto.LicenseBasicRes;
import com.wefunding.ldp.publicdata.construct.license.entity.LicenseBasicEntity;
import com.wefunding.ldp.publicdata.construct.license.mapper.LicenseBasicEntityMapper;
import com.wefunding.ldp.publicdata.construct.license.repository.LicenseBasicEntityRepository;
import com.wefunding.ldp.publicdata.construct.license.service.LicenseBasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yes on 2020/09/29
 */
@Service
public class LicenseBasicServiceImpl implements LicenseBasicService {

    private final LicenseBasicEntityMapper licenseBasicEntityMapper;

    private final LicenseBasicEntityRepository licenseBasicEntityRepository;

    @Autowired
    public LicenseBasicServiceImpl(LicenseBasicEntityMapper licenseBasicEntityMapper, LicenseBasicEntityRepository licenseBasicEntityRepository) {
        this.licenseBasicEntityMapper = licenseBasicEntityMapper;
        this.licenseBasicEntityRepository = licenseBasicEntityRepository;
    }

    @Override
    public void saveLicenseBasic(Gson gson, String result) {
        int start = result.indexOf("item\":");
        int end = result.lastIndexOf("},\"numOfRows");

        String itemString = result.substring(start+"item\":".length(), end);

        Item itemTemp = gson.fromJson(itemString, Item.class);

        LicenseBasicEntity licenseBasicEntityTemp = licenseBasicEntityMapper.toLicenseBasicEntity(itemTemp);
        licenseBasicEntityRepository.save(licenseBasicEntityTemp);

        System.out.println("item save, rnum: " + licenseBasicEntityTemp.getRnum() + ", pageNo: 1");
    }

    @Override
    public void saveAllLicenseBasicList(Gson gson, int totalCount, String result) {
        LicenseBasicRes licenseBasicRes;
        licenseBasicRes = gson.fromJson(result, new TypeToken<LicenseBasicRes>() {
        }.getType());

        List<Item> itemList = licenseBasicRes.getResponse().getBody().getItems().getItem();

        /**
         * save 이용
         */
//                        for (Item item : itemList) {
//                            LicenseBasicEntity licenseBasicEntityTemp = licenseBasicEntityMapper.toLicenseBasicEntity(item);
//                            licenseBasicEntityRepository.save(licenseBasicEntityTemp);
//                            System.out.println("item save, rnum: " + licenseBasicEntityTemp.getRnum() + ", pageNo: " + licenseBasicRes.getResponse().getBody().getPageNo());
//                        }

        /**
         * saveAll 이용
         */
        List<LicenseBasicEntity> licenseBasicEntityListTemp = licenseBasicEntityMapper.toLicenseBasicEntityList(itemList);
        System.out.println(licenseBasicEntityListTemp);
        licenseBasicEntityRepository.saveAll(licenseBasicEntityListTemp);
        System.out.println("itemList saveAll, pageNo: " + licenseBasicRes.getResponse().getBody().getPageNo() + ", totalCount: " + totalCount);
    }
}
