package com.wefunding.ldp.publicdata.construct.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wefunding.ldp.publicdata.construct.entity.LocalCodeEntity;
import com.wefunding.ldp.publicdata.construct.mapper.TestLeeResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import com.wefunding.ldp.publicdata.construct.dto.Item;
import com.wefunding.ldp.publicdata.construct.dto.ResponseRes;
import com.wefunding.ldp.publicdata.construct.entity.TestLeeEntity;
import com.wefunding.ldp.publicdata.construct.entity.TestLeeResultEntity;
import com.wefunding.ldp.publicdata.construct.repository.TestLeeEntityRepository;
import com.wefunding.ldp.publicdata.construct.repository.TestLeeResultEntityRepository;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 이천시 데이터를 이용하여 테스트 하는 api
 */
@RestController
@RequestMapping("/constructLeecheon")
public class ConstructLeecheonController {

    @Autowired
    private TestLeeEntityRepository testLeeEntityRepository;

    @Autowired
    private TestLeeResultEntityRepository testLeeResultEntityRepository;

    @Autowired
    private TestLeeResultMapper testLeeResultMapper;

    private int numOfRows = 2000; // 페이지 당 item 출력 갯수

    private int platGbCd = 0; // 대지

    @Value("${serviceKey}")
    private String serviceKey;

    /*
    이천시 정보 저장된 DB에서 sigungucd, bjdongcd 가져오기 test
     */
    @GetMapping("/getCdTest")
        public List<Item> getCdTest() {

            List<TestLeeEntity> testLeeEntityList = testLeeEntityRepository.findAll();

        for (TestLeeEntity testLeeEntity : testLeeEntityList) {
            Integer id = testLeeEntity.getId();
            String sigungucd = testLeeEntity.getSigungucd();
            String bjdongcd = testLeeEntity.getBjdongcd();

            System.out.println(id + " " + sigungucd + " " + bjdongcd);
        }
        return null;
    }

    /*
    이천시에서 하나의 siguingucd, bjdongcd만 가져와서 한 페이지의 정보 insert test
     */
    @GetMapping("/insertTest")
    @Transactional
    public List<TestLeeResultEntity> insertConstructInfoByOneCdTest() {

        List<TestLeeResultEntity> testLeeResultEntityList = new ArrayList<>();

        ResponseRes responseRes = new ResponseRes();

        try {
            Optional<TestLeeEntity> testLee = testLeeEntityRepository.findById(2);

            if (testLee.isPresent()) {
                String sigungucd = testLee.get().getSigungucd();
                String bjdongcd = testLee.get().getBjdongcd();

                System.out.println(sigungucd + " " + bjdongcd);

                String urlstr = "http://apis.data.go.kr/1611000/BldRgstService/getBrTitleInfo?" +
                        "sigunguCd=" + sigungucd +
                        "&bjdongCd=" + bjdongcd +
                        "&numOfRows=" + numOfRows +
                        "&platGbCd=" + platGbCd +
                        "&_type=json" +
                        "&ServiceKey=" + serviceKey;

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
                System.out.println(result.length());
                Gson gson = new Gson();

                // item이 있는 경우
                if (result.length() > 140) {
                    responseRes = gson.fromJson(result, new TypeToken<ResponseRes>() {
                    }.getType());

                    List<Item> itemList = responseRes.getResponse().getBody().getItems().getItem();

                    for (Item item : itemList) {
                        TestLeeResultEntity testLeeResultEntityTemp = testLeeResultMapper.toTestLeeResultEntity(item);
                        testLeeResultEntityRepository.save(testLeeResultEntityTemp);
                        System.out.println("item save");
                    }

                    System.out.println("item save finish");

                    testLeeResultEntityList = testLeeResultEntityRepository.findAll();

                    System.out.println(testLeeResultEntityList);

                } else { //item이 없는 경우
                    throw new Exception("result count is 0.");
                }

            } else {
                throw new Exception("testLee is null");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return testLeeResultEntityList;
    }

    /*
    이천시 전체 건축 표제부 조회 저장
     */
    @GetMapping("/insertAll")
    @Transactional
    public String insertLeechoenConstruct() {

        List<TestLeeResultEntity> testLeeResultEntityList = new ArrayList<>();
        ResponseRes responseRes = new ResponseRes();
        Gson gson = new Gson();
        int totalCount = 0;

        try {
            List<TestLeeEntity> testLeeEntityList = testLeeEntityRepository.findAll();

            for (TestLeeEntity testLeeEntity : testLeeEntityList) {
                Integer id = testLeeEntity.getId();
                String sigungucd = testLeeEntity.getSigungucd();
                String bjdongcd = testLeeEntity.getBjdongcd();

                System.out.println(id + " " + sigungucd + " " + bjdongcd);

                int pageNo = 1;

                while (true) {

                    String urlstr = "http://apis.data.go.kr/1611000/BldRgstService/getBrTitleInfo?" +
                            "sigunguCd=" + sigungucd +
                            "&bjdongCd=" + bjdongcd +
                            "&platGbCd=" + platGbCd +
                            "&numOfRows=" + numOfRows +
                            "&pageNo=" + pageNo +
                            "&_type=json" +
                            "&ServiceKey=" + serviceKey;

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

                    if (result.length() > 140) {
                        responseRes = gson.fromJson(result, new TypeToken<ResponseRes>(){}.getType());

                        List<Item> itemList = responseRes.getResponse().getBody().getItems().getItem();
                        totalCount = responseRes.getResponse().getBody().getTotalCount().intValue();

                        for (Item item : itemList) {
                            TestLeeResultEntity testLeeResultEntityTemp = testLeeResultMapper.toTestLeeResultEntity(item);
                            testLeeResultEntityRepository.save(testLeeResultEntityTemp);
                            System.out.println("item save, rnum: " + testLeeResultEntityTemp.getRnum() + ", pageNo: " + responseRes.getResponse().getBody().getPageNo());
                        }

                        pageNo++;
                    } else break;

                    if (totalCount / numOfRows + 1 < pageNo) break;
                }
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

        return "Leecheon construct information insert success";
    }
}