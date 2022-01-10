package com.systex.test.demo.model.runner;

import com.systex.test.demo.model.repository.MarketTypeRepository;
import com.systex.test.demo.model.service.MarketTypeService;
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
@Order(value=1)
public class MarketTypeRunner implements CommandLineRunner {
    @Autowired
    MarketTypeRepository marketTypeRepository;
    @Autowired
    MarketTypeService marketTypeService;

    @Override
    public void run(String... args) {
        log.info("MarketType Start");
        // 市場型態: 農 And 漁
        List<String> marketTypes = new LinkedList<>(){{
            add("農");
            add("漁");
        }};
        checkForUpdateData(marketTypes);
        log.info("MarketType Finished");
    }

    public void checkForUpdateData(List<String> marketTypes){
        //檢查資料庫筆數是否為0
        Long count = marketTypeRepository.count();
        if(count <= 0){
            for(String market: marketTypes){
                //建立Api Url
                try {
                    marketTypeService.initApiToData(market, false);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
