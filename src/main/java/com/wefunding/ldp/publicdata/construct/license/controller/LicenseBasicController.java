package com.wefunding.ldp.publicdata.construct.license.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wefunding.ldp.publicdata.construct.common.entity.LocalCodeEntity;
import com.wefunding.ldp.publicdata.construct.common.repository.LocalCodeEntityRepository;
import com.wefunding.ldp.publicdata.construct.license.dto.licensebasicdto.Item;
import com.wefunding.ldp.publicdata.construct.license.dto.licensebasicdto.LicenseBasicRes;
import com.wefunding.ldp.publicdata.construct.license.entity.LicenseBasicEntity;
import com.wefunding.ldp.publicdata.construct.license.mapper.LicenseBasicEntityMapper;
import com.wefunding.ldp.publicdata.construct.license.repository.LicenseBasicEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

/**
 * Created by yes on 2020/09/24
 * 건축물인허가 기본개요 조회 Controller
 */

@RestController
@RequestMapping("/construct/licensebasic")
public class LicenseBasicController {

    private final LocalCodeEntityRepository localCodeEntityRepository;

    private final LicenseBasicEntityRepository licenseBasicEntityRepository;

    private final LicenseBasicEntityMapper licenseBasicEntityMapper;

    private int numOfRows = 2000; // 페이지 당 item 출력 갯수

    private int platGbCd = 0; // 대지

    @Value("${serviceKey}")
    private String serviceKey;

    @Autowired
    public LicenseBasicController(LocalCodeEntityRepository localCodeEntityRepository, LicenseBasicEntityRepository licenseBasicEntityRepository, LicenseBasicEntityMapper licenseBasicEntityMapper) {
        this.localCodeEntityRepository = localCodeEntityRepository;
        this.licenseBasicEntityRepository = licenseBasicEntityRepository;
        this.licenseBasicEntityMapper = licenseBasicEntityMapper;
    }


    @GetMapping("/insert")
    public String getLicenseBasic(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {

        LicenseBasicRes licenseBasicRes;
        Gson gson = new Gson();
        int totalCount = 0;
        int requestCount = 0;

        try {
            List<LocalCodeEntity> localCodeEntityList = localCodeEntityRepository.getLocalCodeEntityList();

            for (LocalCodeEntity localCodeEntity : localCodeEntityList) {
                int depth = Integer.parseInt(localCodeEntity.getDepth());
                int status = Integer.parseInt(localCodeEntity.getStatus());

                Integer id = localCodeEntity.getId();
                String sigungucd = localCodeEntity.getSigunguCd();
                String bjdongcd = localCodeEntity.getBjdongCd();
                String name = localCodeEntity.getName();

                System.out.println("id: " + id + ", sigungucd: " + sigungucd + ", bjdongcd: " + bjdongcd + ", name: " + name + ", depth: " + depth + ", status: " + status);

                int pageNo = 1;

                while (true) {
                    String urlstr = "http://apis.data.go.kr/1611000/ArchPmsService/getApBasisOulnInfo?" +
                            "sigunguCd=" + sigungucd +
                            "&bjdongCd=" + bjdongcd +
                            "&platGbCd=" + platGbCd +
                            "&numOfRows=" + numOfRows +
                            "&startDate=" + startDate +
                            "&endDate=" + endDate +
                            "&pageNo=" + pageNo +
                            "&_type=json" +
                            "&ServiceKey=" + serviceKey;

                    requestCount++;

                    URL url = new URL(urlstr);

                    String result = "";

                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");

                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

                    String returnLine;
                    while ((returnLine = br.readLine()) != null) {
                        result = result.concat(returnLine);
                    }
                    urlConnection.disconnect();

                    System.out.println(result);

                    if (result.length() > 140) {
                        licenseBasicRes = gson.fromJson(result, new TypeToken<LicenseBasicRes>() {
                        }.getType());

                        List<Item> itemList = licenseBasicRes.getResponse().getBody().getItems().getItem();
                        totalCount = licenseBasicRes.getResponse().getBody().getTotalCount().intValue();

                        for (Item item : itemList) {
                            LicenseBasicEntity licenseBasicEntityTemp = licenseBasicEntityMapper.toLicenseBasicEntity(item);
                            licenseBasicEntityRepository.save(licenseBasicEntityTemp);
                            System.out.println("item save, rnum: " + licenseBasicEntityTemp.getRnum() + ", pageNo: " + licenseBasicRes.getResponse().getBody().getPageNo());

                        }
                        pageNo++;
                    } else break;

                    if (totalCount / numOfRows + 1 < pageNo) break;
                }
                System.out.println("request count: " + requestCount);
            }
            System.out.println("item save finished");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "construct License Basic(인허가정보 기본개요) information insert success, requestCount: " + requestCount;
    }
}
