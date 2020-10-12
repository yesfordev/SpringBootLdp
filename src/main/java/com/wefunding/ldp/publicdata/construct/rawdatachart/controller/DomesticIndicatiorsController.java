package com.wefunding.ldp.publicdata.construct.rawdatachart.controller;

import com.wefunding.ldp.publicdata.construct.rawdatachart.dto.DomesticIndicatorsDto;
import com.wefunding.ldp.publicdata.construct.rawdatachart.model.DefaultRes;
import com.wefunding.ldp.publicdata.construct.rawdatachart.service.DomesticIndicatorsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.wefunding.ldp.publicdata.construct.rawdatachart.model.DefaultRes.FAIL_DEFAULT_RES;


/**
 * Created by yes on 2020/10/12
 */

@Slf4j
@RestController
@RequestMapping("rawdata")
public class DomesticIndicatiorsController {

    private final DomesticIndicatorsService domesticIndicatorsService;

    @Autowired
    public DomesticIndicatiorsController(DomesticIndicatorsService domesticIndicatorsService) {
        this.domesticIndicatorsService = domesticIndicatorsService;
    }

    /**
     * 국내지표 전체 조회
     * @return responseEntity(국내 지표, 상태코드)
     */
    @GetMapping("/indicators")
    public ResponseEntity getDomesticIndicatorsList() {
        try {
            DefaultRes<List<DomesticIndicatorsDto>> defaultRes= domesticIndicatorsService.findDomesticIndicatorsList();
            System.out.println("test1");

            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("test2");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
