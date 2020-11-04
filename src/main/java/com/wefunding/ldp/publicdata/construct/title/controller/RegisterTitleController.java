package com.wefunding.ldp.publicdata.construct.title.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wefunding.ldp.publicdata.construct.common.entity.LocalCodeEntity;
import com.wefunding.ldp.publicdata.construct.common.repository.LocalCodeEntityRepository;
import com.wefunding.ldp.publicdata.construct.common.utils.PublicDataUtils;
import com.wefunding.ldp.publicdata.construct.title.dto.RegisterTitleRes;
import com.wefunding.ldp.publicdata.construct.title.entity.RegisterTitleEntity;
import com.wefunding.ldp.publicdata.construct.title.mapper.RegisterTitleMapper;
import com.wefunding.ldp.publicdata.construct.title.dto.Item;
import com.wefunding.ldp.publicdata.construct.title.repository.RegisterTitleEntityRepository;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;

/**
 * 건축물대장 표제부 조회 Controller
 */
@RestController
@RequestMapping("/construct/title")
public class RegisterTitleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterTitleController.class);

    private final RegisterTitleEntityRepository registerTitleEntityRepository;

    private final LocalCodeEntityRepository localCodeEntityRepository;

    private final RegisterTitleMapper registerTitleMapper;

    private final RetryTemplate retryTemplate;

    @Autowired
    public RegisterTitleController(RegisterTitleEntityRepository registerTitleEntityRepository, LocalCodeEntityRepository localCodeEntityRepository, RegisterTitleMapper registerTitleMapper, RetryTemplate retryTemplate) {
        this.registerTitleEntityRepository = registerTitleEntityRepository;
        this.localCodeEntityRepository = localCodeEntityRepository;
        this.registerTitleMapper = registerTitleMapper;
        this.retryTemplate = retryTemplate;
    }

    //test를 위해 2000->10으로 변경
    private int numOfRows = 10; // 페이지 당 item 출력 갯수

    private int platGbCd = 0; // 대지

    @Value("${serviceKey}")
    private String serviceKey;

    /**
     * 기존에 저장되어 있는 법정동 코드 테스트 출
     */
    @GetMapping("/testGetCd")
    public void testGetCd() {

        try {
            List<LocalCodeEntity> localCodeEntityList = localCodeEntityRepository.getLocalCodeEntityList();

            for (LocalCodeEntity localCodeEntity : localCodeEntityList) {
                Integer id = localCodeEntity.getId();
                String sigungucd = localCodeEntity.getSigunguCd();
                String bjdongcd = localCodeEntity.getBjdongCd();
                String name = localCodeEntity.getName();
                int depth = Integer.parseInt(localCodeEntity.getDepth());
                int status = Integer.parseInt(localCodeEntity.getStatus());

                System.out.println("id: " + id + ", sigungucd: " + sigungucd + ", bjdongcd: " + bjdongcd + ", name: " + name + ", depth: " + depth + ", status: " + status);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return;
    }

    @GetMapping("/testGetConstruct")
    public List<RegisterTitleEntity> testGetConstruct() {

        List<RegisterTitleEntity> registerTitleEntityList = new ArrayList<>();

        try {
            List<LocalCodeEntity> localCodeEntityList = localCodeEntityRepository.getSeoulConstruct();

            for (LocalCodeEntity localCodeEntity : localCodeEntityList) {
                Integer id = localCodeEntity.getId();
                String sigungucd = localCodeEntity.getSigunguCd();
                String bjdongcd = localCodeEntity.getBjdongCd();
                String name = localCodeEntity.getName();
                int depth = Integer.parseInt(localCodeEntity.getDepth());
                int status = Integer.parseInt(localCodeEntity.getStatus());

                System.out.println("id: " + id + ", sigungucd: " + sigungucd + ", bjdongcd: " + bjdongcd + ", name: " + name + ", depth: " + depth + ", status: " + status);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return registerTitleEntityList;
    }

    /**
     * 서울의 창고시설만 찾아 DB에 insert하는 api
     * @return 완료 문구, requestCount
     */
    @GetMapping("/insertSeoul")
//    @Transactional
    public String insertSeoulConstruct() {

        RegisterTitleRes registerTitleRes;
        PublicDataUtils publicDataUtils = new PublicDataUtils();
        Gson gson = new Gson();
        int totalCount = 0;
        int requestCount = 0; // 요청 횟수 측정

        try {
            List<LocalCodeEntity> localCodeEntityList = localCodeEntityRepository.getSeoulConstruct();

            for (LocalCodeEntity localCodeEntity : localCodeEntityList) {
                int depth = Integer.parseInt(localCodeEntity.getDepth());
                int status = Integer.parseInt(localCodeEntity.getStatus());

//                if (depth >= 3 && status == 1) {
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


                        String result = "";

                        // IOException 발생 시, 최대 10번까지 재시도 로직
                        result = retryTemplate.execute(context -> publicDataUtils.connectUrl(urlstr));

                        System.out.println("result: " + result);

                        int countStart = result.indexOf("totalCount\":");

                        totalCount = Integer.parseInt(result.substring(countStart + "totalCount\":".length(), result.length() - 3));

                        if (totalCount >= 1) {
                            registerTitleRes = gson.fromJson(result, new TypeToken<RegisterTitleRes>() {
                            }.getType());

                            List<Item> itemList = registerTitleRes.getResponse().getBody().getItems().getItem();
                            totalCount = registerTitleRes.getResponse().getBody().getTotalCount().intValue();

                            for (Item item : itemList) {
                                if (item.getMainPurpsCd().equals("18000")) {
                                    RegisterTitleEntity registerTitleEntityTemp = registerTitleMapper.toRegisterTitleEntity(item);
                                    registerTitleEntityRepository.save(registerTitleEntityTemp);
                                    System.out.println("item save, rnum: " + registerTitleEntityTemp.getRnum() + ", pageNo: " + registerTitleRes.getResponse().getBody().getPageNo());
                                }
                            }
                            pageNo++;
                        } else break;

                        if (totalCount / numOfRows + 1 < pageNo) break;
                    }
//                }
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

        if(!localName.substring(localName.length()-1, localName.length()).equals("시")) {
            return "시 지명을 입력해주세요. ex) 이천시, 서울특별시";
        }

        RegisterTitleRes registerTitleRes;
        PublicDataUtils publicDataUtils = new PublicDataUtils();
        Gson gson = new Gson();
        int totalCount = 0;
        int requestCount = 0;

        try {
            List<LocalCodeEntity> localCodeEntityList = localCodeEntityRepository.getLocalConstruct(localName);

            for (LocalCodeEntity localCodeEntity : localCodeEntityList) {
                int depth = Integer.parseInt(localCodeEntity.getDepth());
                int status = Integer.parseInt(localCodeEntity.getStatus());

//                if (depth >= 3 && status == 1) {
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

                        String result = "";

                        // IOException 발생 시, 최대 10번까지 재시도 로직
                        result = retryTemplate.execute(context -> publicDataUtils.connectUrl(urlstr));

                        System.out.println("result: " + result);

                        int countStart = result.indexOf("totalCount\":");

                        totalCount = Integer.parseInt(result.substring(countStart + "totalCount\":".length(), result.length() - 3));

                        if (totalCount >= 1) {
                            registerTitleRes = gson.fromJson(result, new TypeToken<RegisterTitleRes>() {
                            }.getType());

                            List<Item> itemList = registerTitleRes.getResponse().getBody().getItems().getItem();
                            totalCount = registerTitleRes.getResponse().getBody().getTotalCount().intValue();

                            for (Item item : itemList) {
                                if (item.getMainPurpsCd().equals("18000")) {
                                    RegisterTitleEntity registerTitleEntityTemp = registerTitleMapper.toRegisterTitleEntity(item);
                                    registerTitleEntityRepository.save(registerTitleEntityTemp);
                                    System.out.println("item save, rnum: " + registerTitleEntityTemp.getRnum() + ", pageNo: " + registerTitleRes.getResponse().getBody().getPageNo());
                                }
                            }
                            pageNo++;
                        } else break;

                        if (totalCount / numOfRows + 1 < pageNo) break;
                    }
                }
                System.out.println("request count: " + requestCount);
//            }
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

        RegisterTitleRes responseRes = new RegisterTitleRes();
        PublicDataUtils publicDataUtils = new PublicDataUtils();
        Gson gson = new Gson();
        int totalCount = 0;
        int requestCount = 0;

        try {
            List<LocalCodeEntity> localCodeEntityList = localCodeEntityRepository.getLocalCodeEntityList();

            for (LocalCodeEntity localCodeEntity : localCodeEntityList) {
                int depth = Integer.parseInt(localCodeEntity.getDepth());
                int status = Integer.parseInt(localCodeEntity.getStatus());

//                if (depth >= 3 && status == 1) {
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

                        String result = "";

                        // IOException 발생 시, 최대 10번까지 재시도 로직
                        result = retryTemplate.execute(context -> publicDataUtils.connectUrl(urlstr));

                        System.out.println("result: " + result);

                        int countStart = result.indexOf("totalCount\":");

                        totalCount = Integer.parseInt(result.substring(countStart + "totalCount\":".length(), result.length() - 3));

                        if (totalCount >= 1) {
                            responseRes = gson.fromJson(result, new TypeToken<RegisterTitleRes>() {
                            }.getType());

                            List<Item> itemList = responseRes.getResponse().getBody().getItems().getItem();
                            totalCount = responseRes.getResponse().getBody().getTotalCount().intValue();

                            for (Item item : itemList) {
                                if (item.getMainPurpsCd().equals("18000")) {    // 창고시설
                                    RegisterTitleEntity registerTitleEntityTemp = registerTitleMapper.toRegisterTitleEntity(item);
                                    registerTitleEntityRepository.save(registerTitleEntityTemp);
                                    System.out.println("item save, rnum: " + registerTitleEntityTemp.getRnum() + ", pageNo: " + responseRes.getResponse().getBody().getPageNo());
                                }
                            }
                            pageNo++;
                        } else break;

                        if (totalCount / numOfRows + 1 < pageNo) break;
                    }
                }
                System.out.println("request count: " + requestCount);
//            }
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

    /**
     * [참고] 테스트
     * @return
     */
    @GetMapping("/insertAllTest")
    @Transactional
    public String insertAllTestConstruct() throws IOException {

        PublicDataUtils publicDataUtils = new PublicDataUtils();
        List<LocalCodeEntity> localCodeEntityList = localCodeEntityRepository.getLocalCodeEntityList();

        for (LocalCodeEntity localCodeEntity : localCodeEntityList) {
            int depth = Integer.parseInt(localCodeEntity.getDepth());
            int status = Integer.parseInt(localCodeEntity.getStatus());

            Integer id = localCodeEntity.getId();
            String sigungucd = localCodeEntity.getSigunguCd();
            String bjdongcd = localCodeEntity.getBjdongCd();
            String name = localCodeEntity.getName();

            LOGGER.info("id: " + id + ", sigungucd: " + sigungucd + ", bjdongcd: " + bjdongcd + ", name: " + name + ", depth: " + depth + ", status: " + status);

            int pageNo = 1;

            String urlstr = "http://apis.data.go.kr/1611000/BldRgstService/getBrTitleInfo?" +
                    "sigunguCd=" + sigungucd +
                    "&bjdongCd=" + bjdongcd +
                    "&platGbCd=" + platGbCd +
                    "&numOfRows=" + numOfRows +
                    "&pageNo=" + pageNo +
                    "&_type=json" +
                    "&ServiceKey=" + serviceKey;


            JSONObject resObject = publicDataUtils.connectUrlReturnObject(urlstr);

            //LOGGER.info("RETURN ORIGIN" + resObject);

            ObjectMapper objectMapper01 = new ObjectMapper();
            RegisterTitleRes responseDomain = objectMapper01.readValue(resObject.toString(), RegisterTitleRes.class);

            LOGGER.info("return result: " + responseDomain.getResponse().getHeader().toString());
            LOGGER.info("총 갯수: " + responseDomain.getResponse().getBody().getTotalCount().toString());
            LOGGER.info("총 아이템 갯 수: " + responseDomain.getResponse().getBody().getItems().getItem().size());

        }

        return "test complete";
    }
}
