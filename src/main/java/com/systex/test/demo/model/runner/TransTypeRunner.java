package com.systex.test.demo.model.runner;

import com.systex.test.demo.model.service.TransTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.systex.test.demo.config.DateConverter.*;

@Slf4j
@Component
@Order(value=3)
public class TransTypeRunner implements CommandLineRunner {
    @Autowired
    TransTypeService transTypeTypeService;

    @Override
    public void run(String... args) throws Exception {
        log.info("TransType Start");
        // 市場型態: 農 And 漁
        List<String> marketTypes = new LinkedList<>(){{
            add("農");
            add("漁");
        }};

        //預設抓取兩日內的資料
        Date endDate = new Date();
        //Date startDate = LOCALDATE_TO_DATE(LocalDate.now().minusDays(2));
        //Date startDate = LOCALDATE_TO_DATE(LocalDate.now().minusYears(1));
        Date startDate = LOCALDATE_TO_DATE(LocalDate.now().minusDays(7));

        //檢查資料庫筆數是否需要更新資料
        for(String market: marketTypes){
            //依市場型態建立Api Url
            transTypeTypeService.checkDataStatus(market, startDate, endDate);
        }
        log.info("TransType Finished");
    }
}
