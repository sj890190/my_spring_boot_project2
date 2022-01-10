package com.systex.test.demo.model.service;

import com.systex.test.demo.model.entity.MarketType;
import com.systex.test.demo.model.repository.MarketTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.systex.test.demo.DemoApplication.setUrlToJson;
import static com.systex.test.demo.DemoApplication.setMapToEntity;

@Slf4j
@Service
public class MarketTypeService {
    @Autowired
    MarketTypeRepository marketTypeRepository;

    public void initApiToData(String type, boolean hasMarket) throws IOException, JSONException {
        String url = setApiUrl(type); // 建立Api Url
        Map params = new HashMap();
        Map map = setUrlToJson(url, params); // 檢視Api的狀態, 並回傳Map
        log.info("Check Log For Map Content: {}", map);

        // 檢查上一步驟中, 回傳的Map是否要繼續處理的
        if(map.containsKey("RS")){
            log.info("RS Status Error, map: {}", map);
        }
        // Api回傳的資料中, 狀態有沒有下一頁的, 有則拆分
        if(map.containsKey("Next")){
            log.info("Next Is True: {}", map);
            initApiToData(type, true);
        }else{
            if(!map.containsKey("Data")){
                MarketType marketType;
                for(int i=0; i<map.size(); i++){
                    marketType = setMapToEntity(map, i, MarketType.class);
                    //調整內容至Entity中
                    marketType.setMarketType(type);
                    //寫入SQL DB中
                    marketTypeRepository.save(marketType);
                }
            }
        }
    }

    public String setApiUrl(String type){
        String url = "";
        if(type.equals("農")){
            url = "https://data.coa.gov.tw/api/v1/CropMarketType/";
        }
        if(type.equals("漁")){
            url = "https://data.coa.gov.tw/api/v1/SeafoodProdMarketType/";
        }
        return url;
    }
}
