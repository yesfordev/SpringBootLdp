package com.wefunding.ldp.publicdata.construct.license.controller;

import com.google.gson.Gson;
import com.wefunding.ldp.publicdata.construct.common.entity.LocalCodeEntity;
import com.wefunding.ldp.publicdata.construct.common.repository.LocalCodeEntityRepository;
import com.wefunding.ldp.publicdata.construct.license.dto.licensedongdto.LicenseDongRes;
import com.wefunding.ldp.publicdata.construct.common.utils.PublicDataUtils;
import com.wefunding.ldp.publicdata.construct.license.service.LicenseDongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.List;

/**
 * Created by yes on 2020/09/28
 * 건축인허가 동별개요 조회
 */

@RestController
@RequestMapping("/construct/licensedong")
public class LicenseDongController {

    private int numOfRows = 2000; // 페이지 당 item 출력 갯수

    private int platGbCd = 0; // 대지

    private Integer id;

    @Value("${serviceKey}")
    private String serviceKey;

    private final LocalCodeEntityRepository localCodeEntityRepository;

    private final LicenseDongService licenseDongService;

    private final RetryTemplate retryTemplate;

    @Autowired
    public LicenseDongController(LocalCodeEntityRepository localCodeEntityRepository, LicenseDongService licenseDongService, RetryTemplate retryTemplate) {
        this.localCodeEntityRepository = localCodeEntityRepository;
        this.licenseDongService = licenseDongService;
//        this.connectUrlUtils = connectUrlUtils;
        this.retryTemplate = retryTemplate;
    }

    /**
     * 인허가정보 동별조회 수집
     * @param startDate 검색 시작일
     * @param endDate 검색 종료일
     * @return returnCount, 종료 메세지
     */
    @GetMapping("/insert")
    public String saveLicenseDong(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {

        LicenseDongRes licenseDongRes;
        PublicDataUtils publicDataUtils = new PublicDataUtils();
        Gson gson = new Gson();
        int totalCount = 0;
        int requestCount = 0;

        try {
//            List<LocalCodeEntity> localCodeEntityList = localCodeEntityRepository.getLocalCodeEntityList();
            List<LocalCodeEntity> localCodeEntityList = localCodeEntityRepository.getLocalCodeEntityListById(); // id>=42811
            for (LocalCodeEntity localCodeEntity : localCodeEntityList) {
                int depth = Integer.parseInt(localCodeEntity.getDepth());
                int status = Integer.parseInt(localCodeEntity.getStatus());

                id = localCodeEntity.getId();
                String sigungucd = localCodeEntity.getSigunguCd();
                String bjdongcd = localCodeEntity.getBjdongCd();
                String name = localCodeEntity.getName();

                System.out.println("id: " + id + ", sigungucd: " + sigungucd + ", bjdongcd: " + bjdongcd + ", name: " + name + ", depth: " + depth + ", status: " + status);

                int pageNo = 1;

                while (true) {
                    String urlstr = "http://apis.data.go.kr/1611000/ArchPmsService/getApDongOulnInfo?" +
                            "sigunguCd=" + sigungucd +
                            "&bjdongCd=" + bjdongcd +
                            "&platGbCd=" + platGbCd +
                            "&numOfRows=" + numOfRows +
                            "&startDate=" + startDate +
                            "&endDate=" + endDate +
                            "&pageNo=" + pageNo +
                            "&_type=json" +
                            "&ServiceKey=" + serviceKey;

//                    String urlstr = "http://apis.data.go.kr/1611000/ArchPmsService/getApBasisOulnInfo?sigunguCd=11230&bjdongCd=10300&platGbCd=0&numOfRows=20&_type=json&startDate=20150101&endDate=20150131&ServiceKey=VuQ26MnS93HYgPFpoeyIBLx0uzwHUoWpuroN1MRor5YFc5j6igIR1s8MU46278q8Iz55t3A84JD%2BxS%2F2W%2Fdzvg%3D%3D";

                    String result = "";

                    // IOException 발생 시, 최대 7번까지 재시도 로직
                    result = retryTemplate.execute(context -> publicDataUtils.connectUrl(urlstr));

                    requestCount++;

                    System.out.println("result: " + result);

                    totalCount = publicDataUtils.getTotalCount(result);

                    if (totalCount == 1) {
                        licenseDongService.saveLicenseDong(gson, result);

                        break;
                    } else if (totalCount > 1) {
                        licenseDongService.saveAllLicenseDongList(gson, totalCount, result);

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
        return "construct License Dong(인허가정보 동별) information insert success, requestCount: " + requestCount;
    }

    @ExceptionHandler(NumberFormatException.class)
    public String exceedsRequests(NumberFormatException e) {
        System.err.println(e.getClass());
        e.printStackTrace();
        return "동별 조회의 일일 트래픽이 초과되었습니다. 이어서 수행해야 할 local_code id: " + id;
    }

}
