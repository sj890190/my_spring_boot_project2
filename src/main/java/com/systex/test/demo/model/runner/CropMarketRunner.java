package com.systex.test.demo.model.runner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.systex.test.demo.model.repository.CropMarketRepository;
import com.systex.test.demo.model.service.CropMarketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(value=2)
public class CropMarketRunner implements CommandLineRunner {
    @Autowired
    CropMarketRepository cropMarketRepository;
    @Autowired
    CropMarketService cropMarketService;

    @Override
    public void run(String... args) throws Exception {
        initDB();
    }

    private void initDB() throws JsonProcessingException {
        //log.info("CropMarketRunner Start");
        Long count = cropMarketRepository.count();
        if(count <= 0){
            //建立所有資料
            cropMarketService.getAllData();
        }
    }
}
