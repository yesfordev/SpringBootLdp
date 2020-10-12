package com.wefunding.ldp.publicdata.construct.title.controller;

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
import com.wefunding.ldp.publicdata.construct.title.service.RegisterTitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.bind.annotation.*;

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

    private final RegisterTitleEntityRepository registerTitleEntityRepository;

    private final LocalCodeEntityRepository localCodeEntityRepository;

    private final RegisterTitleMapper registerTitleMapper;

    private final RetryTemplate retryTemplate;

    private final RegisterTitleService registerTitleService;

    @Autowired
    public RegisterTitleController(RegisterTitleEntityRepository registerTitleEntityRepository, LocalCodeEntityRepository localCodeEntityRepository, RegisterTitleMapper registerTitleMapper, RetryTemplate retryTemplate, RegisterTitleService registerTitleService) {
        this.registerTitleEntityRepository = registerTitleEntityRepository;
        this.localCodeEntityRepository = localCodeEntityRepository;
        this.registerTitleMapper = registerTitleMapper;
        this.retryTemplate = retryTemplate;
        this.registerTitleService = registerTitleService;
    }

    private int numOfRows = 2000; // 페이지 당 item 출력 갯수

    private int platGbCd = 0; // 대지

    private Integer id;

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
                id = localCodeEntity.getId();
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
                    id = localCodeEntity.getId();
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
                    id = localCodeEntity.getId();
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
//    @Transactional
    public String insertAllConstruct() {

        PublicDataUtils publicDataUtils = new PublicDataUtils();
        Gson gson = new Gson();
        int totalCount = 0;
        int requestCount = 0;

        try {
//            List<LocalCodeEntity> localCodeEntityList = localCodeEntityRepository.getLocalCodeEntityList();
            List<LocalCodeEntity> localCodeEntityList = localCodeEntityRepository.getLocalCodeEntityListById(); //1113부터(부산) // 2865

            for (LocalCodeEntity localCodeEntity : localCodeEntityList) {
                int depth = Integer.parseInt(localCodeEntity.getDepth());
                int status = Integer.parseInt(localCodeEntity.getStatus());

//                if (depth >= 3 && status == 1) {
                    id = localCodeEntity.getId();
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

                        String result = "";

                        // IOException 발생 시, 최대 7번까지 재시도 로직
                        result = retryTemplate.execute(context -> publicDataUtils.connectUrl(urlstr));

                        requestCount++;

//                        System.out.println("result: " + result);

//                        totalCount = publicDataUtils.getTotalCount(result);
                        String finalResult = result;
                        totalCount = retryTemplate.execute(context ->publicDataUtils.getTotalCount(finalResult));

                        if(totalCount == 1) {
                            registerTitleService.saveRegistserTitle(gson, result);

                            break;
                        } else if (totalCount >1) {
                            registerTitleService.saveRegisterTitleList(gson, totalCount, result);

                            pageNo++;
                        } else break;

                        if (totalCount / numOfRows + 1 < pageNo) break;
                    }
                System.out.println("request count: " + requestCount);
                }
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

//    @ExceptionHandler(NumberFormatException.class)
//    public String exceedsRequests(NumberFormatException e) {
//        System.err.println(e.getClass());
//        e.printStackTrace();
//        return "건축물대장의 표제 조회의 일일 트래픽이 초과되었습니다. 이어서 수행해야 할 local_code id: " + id;
//    }
}
