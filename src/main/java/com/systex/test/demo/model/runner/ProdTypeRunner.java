package com.systex.test.demo.model.runner;

import com.systex.test.demo.model.repository.ProdTypeRepository;
import com.systex.test.demo.model.service.ProdTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;

@Slf4j
@Component
@Order(value=2)
public class ProdTypeRunner implements CommandLineRunner {
    @Autowired
    ProdTypeRepository prodTypeRepository;
    @Autowired
    ProdTypeService prodTypeService;

    @Override
    public void run(String... args) throws Exception {
        initDB();
    }

    private void initDB() throws IOException, ParseException, InterruptedException {
        log.info("ProdType Start");
        Long count = prodTypeRepository.count();
        if(count <= 0){
            //建立所有資料
            prodTypeService.setAllData();
        }
        log.info("ProdType Finished");
    }
}
