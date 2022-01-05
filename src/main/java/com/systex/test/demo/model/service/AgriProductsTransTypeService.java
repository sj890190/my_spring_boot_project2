package com.systex.test.demo.model.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.systex.test.demo.model.entity.AgriProductsTransType;
import com.systex.test.demo.model.entity.CropMarket;
import com.systex.test.demo.model.repository.AgriProductsTransTypeRepository;
import com.systex.test.demo.model.repository.CropMarketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class AgriProductsTransTypeService {
    @Autowired
    AgriProductsTransTypeRepository agriProductsTransTypeRepository;

    @Autowired
    CropMarketRepository cropMarketRepository;

    public void setAgriProductData(){

    }

    public void setAllData(LocalDate startDate, LocalDate endDate) throws IOException, ParseException, InterruptedException {

        String start_Date = (startDate.getYear() - 1911) + "." + String.format("%02d", startDate.getMonthValue()) + "." + String.format("%02d", startDate.getDayOfMonth());
        String end_Date = (endDate.getYear() - 1911) + "." + String.format("%02d", endDate.getMonthValue()) + "." + String.format("%02d", endDate.getDayOfMonth());

        //設置參數
        Map<String, String> params = new HashMap<>();
        for (CropMarket cropMarket : cropMarketRepository.findAll()) {
            String url = "https://data.coa.gov.tw/api/v1/AgriProductsTransType/?Start_time={start_Date}&End_time={end_Date}&MarketName={market_Name}";
            params.put("start_Date", start_Date);
            params.put("end_Date", end_Date);
            params.put("market_Name", cropMarket.getMarketName());
            initApiInfo(url, params);
        }
        log.info("AgriFishery Finished");
    }

    public void initApiInfo(String url, Map<String, String> params) throws ParseException, InterruptedException, JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String body = restTemplate.getForEntity(url, String.class, params).getBody();
        if(body != null){
            setJsonToDB(body);
        }
    }

    public void setJsonToDB(String body) throws InterruptedException, ParseException, JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        List<AgriProductsTransType> agriList = new LinkedList<>();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonParentNode = mapper.readTree(body);

        //log.info(body);
        //轉成JSON Object
        JsonNode jsonChildNode = jsonParentNode.at("/Data");
        if (!jsonChildNode.isMissingNode() && !jsonChildNode.isEmpty()) {

            //data不為空, 且沒有分頁可以存入資料庫
            Iterator<JsonNode> elements = jsonChildNode.elements();
            while (elements.hasNext()) {
                //擷取子節點
                JsonNode node = elements.next();
                //log.info(node.toString());

                AgriProductsTransType agriProductsTransType = mapper.readValue(node.toString(), AgriProductsTransType.class);
                agriProductsTransType.setTranDate(agriProductsTransType.getTransDate());

                log.info(agriProductsTransType.toString());
                agriList.add(agriProductsTransType);

                if (agriList.size() > 500) {
                    agriProductsTransTypeRepository.saveAll(agriList);
                    agriList.clear();
                    Thread.sleep(100);
                }
            }
        }

    }
}
