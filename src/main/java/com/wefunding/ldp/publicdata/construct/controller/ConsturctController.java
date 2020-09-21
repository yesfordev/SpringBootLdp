package com.wefunding.ldp.publicdata.construct.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wefunding.ldp.publicdata.construct.dto.Item;
import com.wefunding.ldp.publicdata.construct.dto.ResponseRes;
import com.wefunding.ldp.publicdata.construct.entity.ConstructTitleEntity;
import com.wefunding.ldp.publicdata.construct.entity.LocalCodeEntity;
import com.wefunding.ldp.publicdata.construct.mapper.ConstructTitleMapper;
import com.wefunding.ldp.publicdata.construct.repository.ConstructTitleEntityRepository;
import com.wefunding.ldp.publicdata.construct.repository.LocalCodeEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

@RestController
@RequestMapping("/construct")
public class ConsturctController {

    private final ConstructTitleEntityRepository constructTitleEntityRepository;

    private final LocalCodeEntityRepository localCodeEntityRepository;

    private final ConstructTitleMapper constructTitleMapper;

    @Autowired
    public ConsturctController(ConstructTitleEntityRepository constructTitleEntityRepository, LocalCodeEntityRepository localCodeEntityRepository, ConstructTitleMapper constructTitleMapper) {
        this.constructTitleEntityRepository = constructTitleEntityRepository;
        this.localCodeEntityRepository = localCodeEntityRepository;
        this.constructTitleMapper = constructTitleMapper;
    }

    private int numOfRows = 2000; // 페이지 당 item 출력 갯수

    private int platGbCd = 0; // 대지

    @Value("${serviceKey}")
    private String serviceKey;

    /**
     * 기존에 저장되어 있는 법정동 코드 테스트 출
     */
    @GetMapping("/testGetCd")
    public void testGetCd() {
        List<ConstructTitleEntity> constructTitleEntityList = new ArrayList<>();
        ResponseRes responseRes = new ResponseRes();
        Gson gson = new Gson();
        int totalCount = 0;

        try {
            List<LocalCodeEntity> localCodeEntityList = localCodeEntityRepository.findAll();

            for (LocalCodeEntity localCodeEntity : localCodeEntityList) {
                Integer id = localCodeEntity.getId();
                String sigungucd = localCodeEntity.getSigunguCd();
                String bjdongcd = localCodeEntity.getBjdongCd();
                String name = localCodeEntity.getName();
                int depth = Integer.parseInt(localCodeEntity.getDepth());
                int status = Integer.parseInt(localCodeEntity.getStatus());

                if (depth >= 3 && status == 1) {
                    System.out.println("id: " + id + ", sigungucd: " + sigungucd + ", bjdongcd: " + bjdongcd + ", name: " + name + ", depth: " + depth + ", status: " + status);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return;
    }

    @GetMapping("/testGetConstruct")
    public List<ConstructTitleEntity> testGetConstruct() {

        List<ConstructTitleEntity> constructTitleEntityList = new ArrayList<>();
        ResponseRes responseRes = new ResponseRes();
        Gson gson = new Gson();
        int totalCount = 0;

        try {
            List<LocalCodeEntity> localCodeEntityList = localCodeEntityRepository.getSeoulConstruct();

            for (LocalCodeEntity localCodeEntity : localCodeEntityList) {
                Integer id = localCodeEntity.getId();
                String sigungucd = localCodeEntity.getSigunguCd();
                String bjdongcd = localCodeEntity.getBjdongCd();
                String name = localCodeEntity.getName();
                int depth = Integer.parseInt(localCodeEntity.getDepth());
                int status = Integer.parseInt(localCodeEntity.getStatus());

                if (depth >= 3 && status == 1) {
                    System.out.println("id: " + id + ", sigungucd: " + sigungucd + ", bjdongcd: " + bjdongcd + ", name: " + name + ", depth: " + depth + ", status: " + status);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return constructTitleEntityList;
    }

    /**
     * 서울의 창고시설만 찾아 DB에 insert하는 api
     * @return 완료 문구, requestCount
     */
    @GetMapping("/insertSeoul")
//    @Transactional
    public String insertSeoulConstruct() {

        ResponseRes responseRes = new ResponseRes();
        Gson gson = new Gson();
        int totalCount = 0;
        int requestCount = 0; // 요청 횟수 측정

        try {
            List<LocalCodeEntity> localCodeEntityList = localCodeEntityRepository.getSeoulConstruct();

            for (LocalCodeEntity localCodeEntity : localCodeEntityList) {
                int depth = Integer.parseInt(localCodeEntity.getDepth());
                int status = Integer.parseInt(localCodeEntity.getStatus());

                if (depth >= 3 && status == 1) {
                    Integer id = localCodeEntity.getId();
                    String sigungucd = localCodeEntity.getSigunguCd();
                    String bjdongcd = localCodeEntity.getBjdongCd();
                    String name = localCodeEntity.getName();

                    System.out.println("id: " + id + ", sigungucd: " + sigungucd + ", bjdongcd: " + bjdongcd + ", name: " + name + ", depth: " + depth + ", status: " + status);

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

                        if (result.length() > 140) {
                            responseRes = gson.fromJson(result, new TypeToken<ResponseRes>() {
                            }.getType());

                            List<Item> itemList = responseRes.getResponse().getBody().getItems().getItem();
                            totalCount = responseRes.getResponse().getBody().getTotalCount().intValue();

                            for (Item item : itemList) {
                                if (item.getMainPurpsCd().equals("18000")) {
                                    ConstructTitleEntity constructTitleEntityTemp = constructTitleMapper.toConstructTitleEntity(item);
                                    constructTitleEntityRepository.save(constructTitleEntityTemp);
                                    System.out.println("item save, rnum: " + constructTitleEntityTemp.getRnum() + ", pageNo: " + responseRes.getResponse().getBody().getPageNo());
                                }
                            }
                            pageNo++;
                        } else break;

                        if (totalCount / numOfRows + 1 < pageNo) break;
                    }
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

        return "construct Seoul information insert success, requestCount: " + requestCount;
    }

    /**
     * 특정 시의 창고시설만 insert하는 api
     * @param localName 찾고 싶은 시
     * @return 완료 문구, requestCount
     */
    @GetMapping("/insertLocal/{localName}")
//    @Transactional
    public String insertLocalConstruct(@PathVariable("localName") String localName) {

        ResponseRes responseRes = new ResponseRes();
        Gson gson = new Gson();
        int totalCount = 0;
        int requestCount = 0;

        try {
            List<LocalCodeEntity> localCodeEntityList = localCodeEntityRepository.getLocalConstruct(localName);

            for (LocalCodeEntity localCodeEntity : localCodeEntityList) {
                int depth = Integer.parseInt(localCodeEntity.getDepth());
                int status = Integer.parseInt(localCodeEntity.getStatus());

                if (depth >= 3 && status == 1) {
                    Integer id = localCodeEntity.getId();
                    String sigungucd = localCodeEntity.getSigunguCd();
                    String bjdongcd = localCodeEntity.getBjdongCd();
                    String name = localCodeEntity.getName();

                    System.out.println("id: " + id + ", sigungucd: " + sigungucd + ", bjdongcd: " + bjdongcd + ", name: " + name + ", depth: " + depth + ", status: " + status);

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

                        if (result.length() > 140) {
                            responseRes = gson.fromJson(result, new TypeToken<ResponseRes>() {
                            }.getType());

                            List<Item> itemList = responseRes.getResponse().getBody().getItems().getItem();
                            totalCount = responseRes.getResponse().getBody().getTotalCount().intValue();

                            for (Item item : itemList) {
                                if (item.getMainPurpsCd().equals("18000")) {
                                    ConstructTitleEntity constructTitleEntityTemp = constructTitleMapper.toConstructTitleEntity(item);
                                    constructTitleEntityRepository.save(constructTitleEntityTemp);
                                    System.out.println("item save, rnum: " + constructTitleEntityTemp.getRnum() + ", pageNo: " + responseRes.getResponse().getBody().getPageNo());
                                }
                            }
                            pageNo++;
                        } else break;

                        if (totalCount / numOfRows + 1 < pageNo) break;
                    }
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

        return "construct " + localName + " information insert success, requestCount: " + requestCount;
    }

    /**
     * 전국의 모든 지역 중 창고시설만 insert하는 api
     * @return
     */
    @GetMapping("/insertAll")
    @Transactional
    public String insertAllConstruct() {

        ResponseRes responseRes = new ResponseRes();
        Gson gson = new Gson();
        int totalCount = 0;
        int requestCount = 0;

        try {
            List<LocalCodeEntity> localCodeEntityList = localCodeEntityRepository.findAll();

            for (LocalCodeEntity localCodeEntity : localCodeEntityList) {
                int depth = Integer.parseInt(localCodeEntity.getDepth());
                int status = Integer.parseInt(localCodeEntity.getStatus());

                if (depth >= 3 && status == 1) {
                    Integer id = localCodeEntity.getId();
                    String sigungucd = localCodeEntity.getSigunguCd();
                    String bjdongcd = localCodeEntity.getBjdongCd();
                    String name = localCodeEntity.getName();

                    System.out.println("id: " + id + ", sigungucd: " + sigungucd + ", bjdongcd: " + bjdongcd + ", name: " + name + ", depth: " + depth + ", status: " + status);

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

                        if (result.length() > 140) {
                            responseRes = gson.fromJson(result, new TypeToken<ResponseRes>() {
                            }.getType());

                            List<Item> itemList = responseRes.getResponse().getBody().getItems().getItem();
                            totalCount = responseRes.getResponse().getBody().getTotalCount().intValue();
만
                            for (Item item : itemList) {
                                if (item.getMainPurpsCd().equals("18000")) {    // 창고시설
                                    ConstructTitleEntity constructTitleEntityTemp = constructTitleMapper.toConstructTitleEntity(item);
                                    constructTitleEntityRepository.save(constructTitleEntityTemp);
                                    System.out.println("item save, rnum: " + constructTitleEntityTemp.getRnum() + ", pageNo: " + responseRes.getResponse().getBody().getPageNo());
                                }
                            }
                            pageNo++;
                        } else break;

                        if (totalCount / numOfRows + 1 < pageNo) break;
                    }
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

        return "construct All information insert success, requestCount: " + requestCount;
    }
}
