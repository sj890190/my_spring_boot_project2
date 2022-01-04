package com.systex.test.demo.model.runner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.systex.test.demo.model.repository.CropTypeRepository;
import com.systex.test.demo.model.service.CropTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(value=1)
public class CropTypeRunner implements CommandLineRunner {
    @Autowired
    CropTypeRepository cropTypeRepository;
    @Autowired
    CropTypeService cropTypeService;

    @Override
    public void run(String... args) throws Exception {
        initDB();
    }

    private void initDB() throws JsonProcessingException {
        //log.info("CropTypeRunner Start");
        Long count = cropTypeRepository.count();
        if(count <= 0){
            //建立所有資料
            cropTypeService.getData();
        }

    }
}
