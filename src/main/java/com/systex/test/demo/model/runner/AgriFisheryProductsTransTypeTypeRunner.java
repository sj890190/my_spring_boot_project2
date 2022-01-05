package com.systex.test.demo.model.runner;

import com.systex.test.demo.model.entity.AgriFisheryProductsTransTypeType;
import com.systex.test.demo.model.repository.AgriFisheryProductsTransTypeTypeRepository;
import com.systex.test.demo.model.service.AgriFisheryProductsTransTypeTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@Component
@Order(value=3)
public class AgriFisheryProductsTransTypeTypeRunner implements CommandLineRunner {
    @Autowired
    AgriFisheryProductsTransTypeTypeRepository agriFisheryProductsTransTypeTypeRepository;
    @Autowired
    AgriFisheryProductsTransTypeTypeService agriFisheryProductsTransTypeTypeService;

    @Override
    public void run(String... args) throws Exception {
        initDB();
    }

    private void initDB() throws IOException, NoSuchFieldException, ParseException, InterruptedException {
        //log.info("AgriFisheryProductsTransTypeType Start");

        LocalDate today = LocalDate.now();
        LocalDate startDate;

        AgriFisheryProductsTransTypeType agriFisheryProductsTransTypeType = agriFisheryProductsTransTypeTypeRepository.findTopByOrderByTranDate();
        //log.info("" + agriFisheryProductsTransTypeType.getTranDate());

        if(agriFisheryProductsTransTypeType == null){
            //都沒資料時, 建立2年內的所有資料
            startDate = today.minusDays(1);
        }else{
            //檢查資料是否為最新狀態
            startDate = agriFisheryProductsTransTypeType.getTranDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        long daysDiff = startDate.until(today, ChronoUnit.DAYS);

        if(daysDiff > 0){
            log.info("today: {}, startDate: {}, diffDays: {}", today, startDate, daysDiff);
            agriFisheryProductsTransTypeTypeService.getAllData(startDate, daysDiff);
        }
    }
}
