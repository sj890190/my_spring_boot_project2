package com.systex.test.demo.model.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.systex.test.demo.model.entity.AgriFisheryProductsTransTypeType;
import com.systex.test.demo.model.entity.CropMarket;
import com.systex.test.demo.model.repository.AgriFisheryProductsTransTypeTypeRepository;
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
public class AgriFisheryProductsTransTypeTypeService {
    @Autowired
    AgriFisheryProductsTransTypeTypeRepository agriFisheryProductsTransTypeTypeRepository;

    @Autowired
    CropMarketRepository cropMarketRepository;

    public void setAgriProductData(){

    }

    public void setAllData(LocalDate startDate, Long daysDiff) throws IOException, ParseException, InterruptedException {

        for(int day= 0; day<=daysDiff; day++){
            LocalDate findDate = startDate.plusDays(day);
            String tranDate = (findDate.getYear() - 1911) +
                    "." + String.format("%02d", findDate.getMonthValue()) +
                    "." + String.format("%02d", findDate.getDayOfMonth());

            //設置參數
            Map<String, String> params = new HashMap<>();
            for (CropMarket cropMarket : cropMarketRepository.findAll()) {
                String url = "https://data.coa.gov.tw/api/v1/AgriFisheryProductsTransTypeType/?TransDate={tran_date}&MarketCode={market_Code}";
                params.put("tran_date", tranDate);
                params.put("market_Code", cropMarket.getMarketCode());
                initApiInfo(url, params, null);
            }
        }
        log.info("AgriFishery Finished");
    }

    public void initApiInfo(String url, Map<String, String> params, String category) throws ParseException, InterruptedException, JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String body = restTemplate.getForEntity(url, String.class, params).getBody();
        if(body != null){
            setJsonToDB(body, category);
        }
    }

    public void setJsonToDB(String body, String category) throws InterruptedException, ParseException, JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        List<AgriFisheryProductsTransTypeType> agriFisherList = new LinkedList<>();

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

                AgriFisheryProductsTransTypeType agriFisheryProductsTransTypeType = mapper.readValue(node.toString(), AgriFisheryProductsTransTypeType.class);
                agriFisheryProductsTransTypeType.setTranDate(agriFisheryProductsTransTypeType.getTransDate());
                if(category != null){
                    agriFisheryProductsTransTypeType.setCategory(category);
                }

                log.info(agriFisheryProductsTransTypeType.toString());
                agriFisherList.add(agriFisheryProductsTransTypeType);

                if (agriFisherList.size() > 500) {
                    agriFisheryProductsTransTypeTypeRepository.saveAll(agriFisherList);
                    agriFisherList.clear();
                    Thread.sleep(100);
                }
            }
        }

    }
}
