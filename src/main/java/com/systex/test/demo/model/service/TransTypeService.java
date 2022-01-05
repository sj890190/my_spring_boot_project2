package com.systex.test.demo.model.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.systex.test.demo.model.entity.ProdType;
import com.systex.test.demo.model.entity.TransType;
import com.systex.test.demo.model.repository.ProdTypeRepository;
import com.systex.test.demo.model.repository.TransTypeRepository;
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
public class TransTypeService {
    @Autowired
    TransTypeRepository transTypeRepository;

    @Autowired
    ProdTypeRepository prodTypeRepository;

    public void setAllData(LocalDate startDate, Long daysDiff) throws IOException, ParseException, InterruptedException {
        //設置參數 - 農
        initApiType(startDate, daysDiff, "農");

        //設置參數 - 漁
        initApiType(startDate, daysDiff, "漁");
    }

    public void initApiType(LocalDate startDate, Long daysDiff, String type)
            throws ParseException, InterruptedException, JsonProcessingException {
        String url = "";
        String symbol = "";
        //設置參數 - 漁
        if(type.equals("漁")){
            symbol = "";
            url = "https://data.coa.gov.tw/api/v1/FisheryProductsTransType/?Start_time={start_date}&End_time={end_date}&SeafoodProdCode={prod_Code}";
        }

        //設置參數 - 農
        if(type.equals("農")){
            url = "https://data.coa.gov.tw/api/v1/AgriProductsTransType/?Start_time={start_date}&End_time={end_date}&CropCode={prod_Code}";
            symbol = ".";
        }

        for(int day= 0; day<=daysDiff; day++){
            Map<String, String> params = new HashMap<>();
            LocalDate findDate = startDate.plusDays(day);
            String tranDate = (findDate.getYear() - 1911) + symbol
                    + String.format("%02d", findDate.getMonthValue()) + symbol
                    + String.format("%02d", findDate.getDayOfMonth());
            params.put("start_date", tranDate);
            params.put("end_date", tranDate);

            Iterator<ProdType> prodTypeIterator = prodTypeRepository.findAllByProdType(type).iterator();
            while (prodTypeIterator.hasNext()) {
                ProdType prodType = prodTypeIterator.next();
                params.put("prod_Code", prodType.getProdCode());
                initApiInfo(url, params, type, startDate);
            }
        }

    }

    public void initApiInfo(String url, Map<String, String> params, String type, LocalDate startDate)
            throws ParseException, JsonProcessingException, InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        String body = restTemplate.getForEntity(url, String.class, params).getBody();
        if(body != null){
            setJsonToDB(body, type, startDate);
        }
    }

    public void setJsonToDB(String body, String type, LocalDate startDate)
            throws ParseException, JsonProcessingException, InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        List<TransType> agriFisherList = new LinkedList<>();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonParentNode = mapper.readTree(body);


        if (jsonParentNode.at("/Next").asText().equals("true")) {
            //有分頁, 需要增加API的參數
            log.info("Has Next: True!!!!!!!!!!!!");
            log.info(jsonParentNode.at("/data").asText());
        } else {
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

                    TransType transType = mapper.readValue(node.toString(), TransType.class);
                    transType.setTranDate(transType.getTransDate().toString());
                    transType.setProdType(type);

                    log.info(transType.toString());
                    agriFisherList.add(transType);

                    if (agriFisherList.size() > 500) {
                        transTypeRepository.saveAll(agriFisherList);
                        agriFisherList.clear();
                    }
                }
                transTypeRepository.saveAll(agriFisherList);
                agriFisherList.clear();
            }
        }

    }
}
