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
import java.util.Date;

import static com.systex.test.demo.config.DateConverter.DATE_TO_LOCALDATE;

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
    public void run(Date date) throws Exception {
        LocalDate localDate = DATE_TO_LOCALDATE(date);
        initDB(localDate);
    }
    public void run(LocalDate localDate) throws Exception {
        initDB(localDate);
    }

    private void initDB() throws IOException, ParseException, InterruptedException {
        log.info("TransType Start");
        LocalDate today = LocalDate.now();
        LocalDate startDate;

        TransType transType = transTypeTypeRepository.findTopByOrderByTransDateDesc();
        //log.info("" + TransType.getTranDate());

        if(transType == null){
            //都沒資料時, 建立1個月內的所有資料
            //startDate = today.minusMonths(1);
            startDate = today.minusYears(1);
        }else{
            //檢查資料是否為最新狀態
            startDate = transType.getTranDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        long daysDiff = startDate.until(today, ChronoUnit.DAYS);

        if(daysDiff > 1){
            log.info("today: {}, startDate: {}, diffDays: {}", today, startDate, daysDiff);
            transTypeTypeService.setAllData(startDate, daysDiff);
        }
        log.info("TransType Finished");
    }

    private void initDB(LocalDate localDate) throws IOException, ParseException, InterruptedException {
        log.info("TransType Start");
        LocalDate today = LocalDate.now();
        LocalDate startDate;

        TransType transType = transTypeTypeRepository.findTopByOrderByTransDateDesc();
        //log.info("" + TransType.getTranDate());

        if(transType == null){
            //都沒資料時, 建立1個月內的所有資料
            //startDate = today.minusMonths(1);
            startDate = today.minusYears(1);
        }else{
            //檢查資料是否為最新狀態
            startDate = transType.getTranDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        if(localDate.until(startDate, ChronoUnit.DAYS) > 0){
            startDate = localDate;
        }
        long daysDiff = startDate.until(today, ChronoUnit.DAYS);

        if(daysDiff > 1){
            log.info("today: {}, startDate: {}, diffDays: {}", today, startDate, daysDiff);
            transTypeTypeService.setAllData(startDate, daysDiff);
        }
        log.info("TransType Finished");
    }
}
