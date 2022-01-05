package com.systex.test.demo.model.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.systex.test.demo.model.entity.CropMarket;
import com.systex.test.demo.model.repository.CropMarketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class CropMarketService {
    @Autowired
    private CropMarketRepository cropMarketRepository;

    public void getAllData() throws JsonProcessingException {

        RestTemplate restTemplate = new RestTemplate();
        List<CropMarket> cropList = new LinkedList<>();

        //設置參數
        String url = "https://data.coa.gov.tw/api/v1/CropMarketType/";
        String body = restTemplate.getForEntity(url, String.class).getBody();
        //System.out.println(body);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonParentNode = mapper.readTree(body);

        //System.out.println(jsonParentNode.at("/RS"));
        if(jsonParentNode.isMissingNode() || !jsonParentNode.at("/RS").asText().equals("OK")){
            //未正確取得API資料
        }

        //System.out.println(jsonParentNode.at("/Next"));
        if(jsonParentNode.at("/Next").asText().equals("true")){
            //有分頁, 需要增加API的參數
        }

        //轉乘JSON Object
        JsonNode jsonChildNode = jsonParentNode.at("/Data");
        if(!jsonChildNode.isMissingNode() && !jsonChildNode.isEmpty()){

            //data不為空, 且沒有分頁可以存入資料庫
            Iterator<JsonNode> elements = jsonChildNode.elements();
            while(elements.hasNext()){
                //擷取子節點
                JsonNode node = elements.next();
                //System.out.println(node.toString());

                CropMarket cropMarket = mapper.readValue(node.toString(), CropMarket.class);
                cropList.add(cropMarket);
            }
        }

        if(!cropList.isEmpty()){
            cropMarketRepository.saveAll(cropList);
        }
        log.info("cropMark Finished");
    }
}