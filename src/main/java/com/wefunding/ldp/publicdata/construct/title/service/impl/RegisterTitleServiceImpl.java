package com.wefunding.ldp.publicdata.construct.title.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wefunding.ldp.publicdata.construct.title.dto.Item;
import com.wefunding.ldp.publicdata.construct.title.dto.RegisterTitleRes;
import com.wefunding.ldp.publicdata.construct.title.entity.RegisterTitleEntity;
import com.wefunding.ldp.publicdata.construct.title.mapper.RegisterTitleMapper;
import com.wefunding.ldp.publicdata.construct.title.repository.RegisterTitleEntityRepository;
import com.wefunding.ldp.publicdata.construct.title.service.RegisterTitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yes on 2020/10/06
 */
@Service
public class RegisterTitleServiceImpl implements RegisterTitleService {

    private final RegisterTitleMapper registerTitleMapper;

    private final RegisterTitleEntityRepository registerTitleEntityRepository;

    @Autowired
    public RegisterTitleServiceImpl(RegisterTitleMapper registerTitleMapper, RegisterTitleEntityRepository registerTitleEntityRepository) {
        this.registerTitleMapper = registerTitleMapper;
        this.registerTitleEntityRepository = registerTitleEntityRepository;
    }

    @Override
    public void saveRegistserTitle(Gson gson, String result) {
        int start = result.indexOf("item\":");
        int end = result.lastIndexOf("},\"numOfRows");

        String itemString = result.substring(start + "item\":".length(), end);

        Item itemTemp = gson.fromJson(itemString, Item.class);

        if(itemTemp.getMainPurpsCd().equals("18000")) {
            RegisterTitleEntity registerTitleEntityTemp = registerTitleMapper.toRegisterTitleEntity(itemTemp);
            registerTitleEntityRepository.save(registerTitleEntityTemp);
            System.out.println("item save, rnum: " + registerTitleEntityTemp.getRnum() + ", pageNo: 1");
        }
    }

    @Override
    public void saveRegisterTitleList(Gson gson, int totalCount, String result) {
        RegisterTitleRes registerTitleRes = gson.fromJson(result, new TypeToken<RegisterTitleRes>() {
        }.getType());

        List<Item> itemList = registerTitleRes.getResponse().getBody().getItems().getItem();

        for (Item item : itemList) {
            if (item.getMainPurpsCd().equals("18000")) {    // 창고시설
                RegisterTitleEntity registerTitleEntityTemp = registerTitleMapper.toRegisterTitleEntity(item);
                registerTitleEntityRepository.save(registerTitleEntityTemp);
                System.out.println("item save, rnum: " + registerTitleEntityTemp.getRnum() + ", pageNo: " + registerTitleRes.getResponse().getBody().getPageNo());
            }
        }
    }
}
