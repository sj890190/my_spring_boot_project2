package com.systex.test.demo.model.runner;

import com.systex.test.demo.model.entity.TransType;
import com.systex.test.demo.model.repository.TransTypeRepository;
import com.systex.test.demo.model.service.TransTypeService;
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
public class TransTypeRunner implements CommandLineRunner {
    @Autowired
    TransTypeRepository transTypeTypeRepository;
    @Autowired
    TransTypeService transTypeTypeService;

    @Override
    public void run(String... args) throws Exception {
        initDB();
    }

    private void initDB() throws IOException, ParseException, InterruptedException {
        log.info("TransType Start");
        LocalDate today = LocalDate.now();
        LocalDate startDate;

        TransType transType = transTypeTypeRepository.findTopByOrderByTransDateDesc();
        //log.info("" + TransType.getTranDate());

        if(transType == null){
            //都沒資料時, 建立1個月內的所有資料
            startDate = today.minusMonths(1);
        }else{
            //檢查資料是否為最新狀態
            startDate = transType.getTranDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        long daysDiff = startDate.until(today, ChronoUnit.DAYS);

        if(daysDiff > 0){
            log.info("today: {}, startDate: {}, diffDays: {}", today, startDate, daysDiff);
            transTypeTypeService.setAllData(startDate, daysDiff);
        }
        log.info("TransType Finished");
    }
}
