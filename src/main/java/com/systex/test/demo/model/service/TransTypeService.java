package com.systex.test.demo.model.service;

import com.systex.test.demo.model.entity.MarketType;
import com.systex.test.demo.model.entity.ProdType;
import com.systex.test.demo.model.entity.TransType;
import com.systex.test.demo.model.repository.MarketTypeRepository;
import com.systex.test.demo.model.repository.ProdTypeRepository;
import com.systex.test.demo.model.repository.TransTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.systex.test.demo.DemoApplication.*;
import static com.systex.test.demo.config.DateConverter.*;

@Slf4j
@Service
public class TransTypeService {
    @Autowired
    TransTypeRepository transTypeRepository;

    @Autowired
    ProdTypeRepository prodTypeRepository;

    @Autowired
    MarketTypeRepository marketTypeRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    //設置Api的選項, 並透過setApiUrl將Api的參數給Url Map
    public void initOptionToUrl(String type, boolean hasMarket, boolean hasProd, String tranDate) throws IOException, JSONException {
        //市場清單
        List<String> marketNameList = new LinkedList<>();
        if (hasMarket) {
            //是否需要開啟市場名稱的參數
            Iterable<MarketType> marketTypeIterable = marketTypeRepository.findAllByMarketType(type);
            marketNameList = StreamSupport.stream(marketTypeIterable.spliterator(), false)
                    .map(MarketType::getMarketName).collect(Collectors.toList());
        }
        //產品清單
        List<String> prodCodeList = new LinkedList<>();
        if (hasProd) {
            //是否需要開啟產品代碼的參數
            Iterable<ProdType> prodTypeIterable = prodTypeRepository.findAllByProdType(type);
            prodCodeList = StreamSupport.stream(prodTypeIterable.spliterator(), false)
                    .map(ProdType::getProdCode).collect(Collectors.toList());
        }
        //設置Url必要參數至Map中
        Map<Object, Object> map = new HashMap<>();
        map.put("start_date", tranDate);
        map.put("end_date", tranDate);
        setApiUrl(type, map, marketNameList, prodCodeList); // 建立Api Url Map
    }
    //將Api的參數給Url Map
    public void setApiUrl(String type, Map param, List<String> marketNameList, List<String> prodCodeList) throws JSONException, IOException {
        String url = "", prodTitle = "", marketTitle = "&MarketName={MarketName}";
        String tranDate = param.get("start_date").toString();
        Queue<Map> dataList = new LinkedList<>();

        if(type.equals("農")){
            url = "https://data.coa.gov.tw/api/v1/AgriProductsTransType/";
            prodTitle = "&CropCode={prodCode}";
        }
        if(type.equals("漁")){
            url = "https://data.coa.gov.tw/api/v1/FisheryProductsTransType/";
            prodTitle= "&SeafoodProdCode={prodCode}";
        }
        url = url + "?Start_time={start_date}&End_time={end_date}";

        dataList.add(param);

        //存放多筆條件實的Map參數
        Queue<Map> paramList = new LinkedList<>();

        boolean hasMarket = false;
        if(marketNameList.size() != 0){
            hasMarket = true;
            url = url + marketTitle;
            //log.info("url: {}", url);
            for(int idx=0; idx<dataList.size(); idx++){
                Map params = dataList.poll();
                for(String marketName: marketNameList){
                    Map addParam = new HashMap(params);
                    addParam.put("MarketName", marketName);
                    //log.info("addParam: {}", addParam);
                    paramList.add(addParam);
                }
            }
            dataList = paramList;
            log.info("Finished Add MarketList");
        }

        if(prodCodeList.size() != 0){
            url = url + prodTitle;
            //log.info("url: {}", url);
            for(String prodCode: prodCodeList){
                Map addParam = new HashMap(param);
                addParam.put("prodCode", prodCode);
                paramList.add(addParam);
            }
            dataList = paramList;
            log.info("Finished Add ProdList");
        }

        //將Map透過Url取得Json Data
        for(int idx=0; idx<dataList.size(); idx++){
            Map params = dataList.poll();
            Map dataMap = setUrlToJson(url, params); // 檢視Api的狀態, 並回傳Map

            if(dataMap.containsKey("Next")){
                log.info("Next Is True: {}", dataMap);
                if(!hasMarket){
                    //若有分頁, 則添加市場
                    initOptionToUrl(type, true, false, tranDate);
                } else{
                    //若有分頁, 則添加產品
                    initOptionToUrl(type, true, true, tranDate);
                }
            }else{
                if(!dataMap.isEmpty()){
                    initUrlToData(type, dataMap, tranDate);
                }
            }
        }
    }
    //透過Url取得Json Data
    public void initUrlToData(String type, Map dataMap, String tranDate) {

        // 檢查上一步驟中, 回傳的Map是否要繼續處理的
        if(dataMap.containsKey("RS")){
            log.info("RS Status Error, map: {}", dataMap);
        }
        // Api回傳的資料中, 狀態沒有下一頁的拆分
        //log.info("Check Log For Map Content: {}", dataMap);
        if(!dataMap.containsKey("Data")){
            TransType transType;
            for(int i=0; i<dataMap.size(); i++){
                transType = setMapToEntity(dataMap, i, TransType.class);

                //調整內容至Entity中
                transType.setProdType(type);//產品型態
                if(transType.getMarketCode() == null){//產品代碼
                    String marketName = transType.getMarketName();
                    //取得市場名稱對應市場代碼
                    transType.setMarketCode(marketTypeRepository.findTopByMarketName(marketName).getMarketCode());
                }
                try {
                    transType.setTranDate(tranDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //寫入SQL DB中
                log.info("Insert To DB: {}", transType);
                transTypeRepository.save(transType);
            }
        }
    }
    //檢查資料是否為最新狀態
    public void checkDataStatus(String marketType, Date date_start, Date data_end){
        //LocalDate 與 Date轉換成相同格式後, 取得差異天數
        long diffDays = DATE_DIFF(date_start, data_end);
        //依照差異的天數每天比較
        for(int day = 0; day<diffDays; day++){
            LocalDate checkDate = DATE_TO_LOCALDATE(date_start).plusDays(day);
            String transDate = LOCALDATE_TO_STRING(checkDate, marketType);

            if(checkDate != LocalDate.now()){ //若不等於當日則檢查有無筆數, 沒有則更新
                if(transTypeRepository.countByTransDate(transDate) <= 0){
                    updateData(marketType, transDate);
                }
            }else{//若等於當日則強制更新
                updateData(marketType, transDate);
            }
        }
    }
    //更新資料庫資料
    public void updateData(String marketType, String transDate){
        try {
            initOptionToUrl(marketType, false, false, transDate);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<Map<String, Object>> queryExercise(String sql, Object[] o) {
        log.info("queryExercise:");
        List arrList = Arrays.stream(o).collect(Collectors.toList());
        log.info("array: {}", arrList.toString());
        //for(int i = 0; i<o.length; i++){
        //    log.info(o[i].toString());
        //}
        List<Map<String, Object>> res = jdbcTemplate.queryForList(sql, o);
        return res;
    }
}
