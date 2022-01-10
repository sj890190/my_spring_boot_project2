package com.systex.test.demo.model.runner;

import com.systex.test.demo.model.repository.ProdTypeRepository;
import com.systex.test.demo.model.service.ProdTypeService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component
@Order(value=2)
public class ProdTypeRunner implements CommandLineRunner {
    @Autowired
    ProdTypeRepository prodTypeRepository;
    @Autowired
    ProdTypeService prodTypeService;

    @Override
    public void run(String... args) {
        log.info("ProdType Start");
        // 市場型態: 農 And 漁
        List<String> prodTypes = new LinkedList<>(){{
            add("農");
            add("漁");
        }};
        checkForUpdateData(prodTypes);
        log.info("ProdType Finished");
    }

    public void checkForUpdateData(List<String> prodTypes){
        //檢查資料庫筆數是否為0
        Long count = prodTypeRepository.count();
        if(count <= 0){
            for(String prodType: prodTypes){
                //建立Api Url
                try {
                    prodTypeService.initApiToData(prodType, false);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
