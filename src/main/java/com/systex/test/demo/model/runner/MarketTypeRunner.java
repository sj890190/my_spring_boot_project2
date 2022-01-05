package com.systex.test.demo.model.runner;

import com.systex.test.demo.model.repository.MarketTypeRepository;
import com.systex.test.demo.model.service.MarketTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@Order(value=1)
public class MarketTypeRunner implements CommandLineRunner {
    @Autowired
    MarketTypeRepository marketTypeRepository;
    @Autowired
    MarketTypeService marketTypeService;

    @Override
    public void run(String... args) throws Exception {
        initDB();
    }

    private void initDB() throws IOException {
        log.info("MarketType Start");
        Long count = marketTypeRepository.count();
        if(count <= 0){
            //建立所有資料
            marketTypeService.setAllData();
        }
        log.info("MarketType Finished");
    }
}
