package com.systex.test.demo.model.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.systex.test.demo.model.entity.MarketType;
import com.systex.test.demo.model.repository.MarketTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class MarketTypeService {
    @Autowired
    MarketTypeRepository marketTypeRepository;

    public void setAllData() throws IOException {
        String url = "";
        //設置參數 - 農
        url = "https://data.coa.gov.tw/api/v1/CropMarketType/";
        initApiInfo(url, "農");

        //設置參數 - 漁
        url = "https://data.coa.gov.tw/api/v1/SeafoodProdMarketType/";
        initApiInfo(url, "漁");
    }

    public void initApiInfo(String url, String type) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String body = restTemplate.getForEntity(url, String.class).getBody();
        if(body != null){
            setJsonToDB(body, type);
        }
    }

    public void setJsonToDB(String body, String type) throws JsonProcessingException {
        List<MarketType> marketTypeList = new LinkedList<>();

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

                MarketType marketType = mapper.readValue(node.toString(), MarketType.class);
                marketType.setMarketType(type);

                log.info(marketType.toString());
                marketTypeList.add(marketType);

                if (marketTypeList.size() > 500) {
                    marketTypeRepository.saveAll(marketTypeList);
                    marketTypeList.clear();
                }
            }
            marketTypeRepository.saveAll(marketTypeList);
            marketTypeList.clear();
        }
    }
}
