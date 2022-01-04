package com.systex.test.demo.model.service;

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

    public void getAllData(LocalDate startDate, Long daysDiff) throws IOException, ParseException, InterruptedException {

        RestTemplate restTemplate = new RestTemplate();

        for(int day=0; day<=daysDiff; day++){
            LocalDate findDate = startDate.plusDays(day);
            String tranDate = (findDate.getYear() - 1911) +
                    "." + String.format("%02d", findDate.getMonthValue()) +
                    "." + String.format("%02d", findDate.getDayOfMonth());

            //設置參數
            Map<String, String> params = new HashMap<>();
            params.put("tran_date",tranDate);

            List<AgriFisheryProductsTransTypeType> agriFisherList = new LinkedList<>();
            for (CropMarket cropMarket : cropMarketRepository.findAll()) {
                //log.info(cropMarket.getMarketCode());
                params.put("market_Code", cropMarket.getMarketCode());
                String url = "https://data.coa.gov.tw/api/v1/AgriFisheryProductsTransTypeType/?TransDate={tran_date}&MarketCode={market_Code}";
                String body = restTemplate.getForEntity(url, String.class, params).getBody();
                //log.info(body);

                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonParentNode = mapper.readTree(body);

                //log.info(jsonParentNode.at("/RS"));
                if (jsonParentNode.isMissingNode() || !jsonParentNode.at("/RS").asText().equals("OK")) {
                    //未正確取得API資料
                    continue;
                }

                //System.out.println(jsonParentNode.at("/Next"));
                if (jsonParentNode.at("/Next").asText().equals("true")) {
                    //有分頁, 需要增加API的參數
                    log.info("Has Next: True!!!!!!!!!!!!1");
                    continue;
                }

                //轉乘JSON Object
                JsonNode jsonChildNode = jsonParentNode.at("/Data");
                if (!jsonChildNode.isMissingNode() && !jsonChildNode.isEmpty()) {

                    //data不為空, 且沒有分頁可以存入資料庫
                    Iterator<JsonNode> elements = jsonChildNode.elements();
                    while (elements.hasNext()) {
                        //擷取子節點
                        JsonNode node = elements.next();
                        //System.out.println(node.toString());

                        AgriFisheryProductsTransTypeType agriFisheryProductsTransTypeType = mapper.readValue(node.toString(), AgriFisheryProductsTransTypeType.class);
                        agriFisheryProductsTransTypeType.setTranDate(agriFisheryProductsTransTypeType.getTransDate());

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
        log.info("AgriFishery Finished");
    }
}
