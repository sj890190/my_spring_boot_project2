package com.systex.test.demo.model.runner;

import com.systex.test.demo.model.entity.AgriProductsTransType;
import com.systex.test.demo.model.repository.AgriProductsTransTypeRepository;
import com.systex.test.demo.model.service.AgriProductsTransTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Slf4j
@Component
@Order(value=3)
public class AgriProductsTransTypeRunner implements CommandLineRunner {
    @Autowired
    AgriProductsTransTypeRepository agriProductsTransTypeRepository;
    @Autowired
    AgriProductsTransTypeService agriProductsTransTypeService;

    @Override
    public void run(String... args) throws Exception {
        initDB();
    }

    private void initDB() throws IOException, ParseException, InterruptedException {
        //log.info("AgriFisheryProductsTransTypeType Start");

        LocalDate today = LocalDate.now();
        LocalDate startDate;

        AgriProductsTransType agriProductsTransType = agriProductsTransTypeRepository.findTopByOrderByTranDateDesc();
        //log.info("" + agriProductsTransType.getTranDate());

        if(agriProductsTransType == null){
            //都沒資料時, 建立2年內的所有資料
            startDate = today.minusDays(1);
        }else{
            //檢查資料是否為最新狀態
            startDate = agriProductsTransType.getTranDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }

        log.info("startDate: {}, today: {}", startDate, today);
        agriProductsTransTypeService.setAllData(startDate, today);
    }
}
