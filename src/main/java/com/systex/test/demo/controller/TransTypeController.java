package com.systex.test.demo.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.systex.test.demo.model.repository.TransTypeRepository;
import com.systex.test.demo.model.service.TransTypeService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static com.systex.test.demo.config.DateConverter.DATE_TO_LOCALDATE;

@Slf4j
@RestController
@RequestMapping("/api/TransType")
public class TransTypeController {

    @Autowired
    TransTypeService transTypeService;

    @Autowired
    TransTypeRepository transTypeRepository;

    private final static String EXERCISE_01 = "SELECT MIN(trans_quantity) AS minQty, MAX(trans_quantity) AS maxQty, AVG(trans_quantity) AS avgQty" +
            " FROM trans_type WHERE tran_date BETWEEN ? AND ? AND prod_type = ? AND prod_name = ? AND market_name = ? GROUP BY prod_name";

    @ResponseBody
    @GetMapping("/queryExercise_01")
    public JsonNode getExercise01(@RequestParam(value = "Start_Date", defaultValue = "2022-01-05") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date_start,
                                  @RequestParam(value = "End_Date", defaultValue = "2022-01-06") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date_end)
            throws ParseException, JsonProcessingException, InterruptedException {
        //輸入起始時間跟結束時間，顯示這段時間農產品中的"椰子"在"台北二"這個市場中的平均交易量、最大交易量、最低交易量
        String type = "農";
        String prodName = "椰子";
        String marketName = "台北二";
        transTypeService.initApiType(date_start, date_end, type, prodName, marketName);
        Object[] o = {date_start, date_end, type, prodName, marketName};

        JsonNode node = new ObjectMapper().valueToTree(transTypeService.queryExercise(EXERCISE_01, o).toString());
        log.info(node.toString());
        return node;
    }


    private final static String EXERCISE_02 = "SELECT prod_type, SUM(avg_price * trans_quantity) AS total_price" +
            " FROM trans_type WHERE tran_date = ? GROUP BY prod_type";

    @ResponseBody
    @GetMapping(value = "/queryExercise_02")
    public JsonNode getExercise02(@RequestParam(value = "Start_Date", defaultValue = "2022-01-06") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date_start) throws ParseException, JsonProcessingException, InterruptedException {
        //輸入指定日期，顯示這一天漁產品的總交易金額跟農產品交易總交易金額大小
        Long daysDiff = 0L;
        LocalDate start_date = date_start;
        transTypeService.initApiType(start_date, daysDiff, "漁");
        Object[] o = {start_date};

        JsonNode node = new ObjectMapper().valueToTree(transTypeService.queryExercise(EXERCISE_02, o).toString());
        log.info(node.toString());
        return node;
    }

    private final static String EXERCISE_03 = "SELECT tran_date, market_name, prod_name, trans_quantity" +
            " FROM trans_type WHERE tran_date BETWEEN ? AND ? AND market_name = ? AND prod_name = ? AND prod_type = ? ORDER BY tran_date";

    @ResponseBody
    @GetMapping(value = "/queryExercise_03")
    public JsonNode getExercise03(@RequestParam(value = "Start_Date", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date_tran,
                                  @RequestParam(value = "Market_Name") String market_name, @RequestParam(value = "Prod_Name") String prod_name) throws IOException, JSONException {
        //輸入指定日期跟市場與農產品，顯示過去五天的交易量是否為"嚴格遞增"
        LocalDate start_date = date_tran.minusDays(5);
        LocalDate end_date = start_date.plusDays(4);
        Object[] o = {start_date, end_date, market_name, prod_name, "農"};
        //LocalDate TIMESTAMP_TO_LOCALDATE(timestamp);

        boolean isTrue = true;
        Queue<Integer> queue = new LinkedList();
        List<Map<String, Object>> list = transTypeService.queryExercise(EXERCISE_03, o);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            int trans_quantity = Integer.parseInt(map.get("trans_quantity").toString());
            if (queue.isEmpty()) {
                queue.add(trans_quantity);
                continue;
            }
            if (!(queue.poll() < trans_quantity)) {
                isTrue = false;
                break;
            }
        }

        //JSONObject jsonObject = new JSONObject();
        String jsonStr = "";
        if (isTrue) {
            //jsonObject.put("嚴格遞增", "是");
            jsonStr = "{\"嚴格遞增\":\"是\"}";
        } else {
            //jsonObject.put("嚴格遞增", "否");
            jsonStr = "{\"嚴格遞增\":\"否\"}";
        }
        JsonNode node = new ObjectMapper().readTree(jsonStr);
        return node;
    }

    private final static String EXERCISE_04 = "SELECT prod_name, SUM(trans_quantity)" +
            "FROM trans_type WHERE tran_date BETWEEN ? AND ? AND prod_type = ?" +
            " GROUP BY prod_name ORDER BY SUM(trans_quantity) DESC LIMIT 10";

    @ResponseBody
    @GetMapping(value = "/queryExercise_04")
    public JsonNode getExercise04(@RequestParam(value = "Tran_Date", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date_tran) {
        //輸入指定日期跟市場與農產品，顯示過去五天的交易量是否為"嚴格遞增"
        LocalDate start_date = date_tran.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate end_date = date_tran.with(TemporalAdjusters.lastDayOfMonth());
        log.info("start_date: {}, end_date: {}", start_date, end_date);
        Object[] o = {start_date, end_date, "農"};
        JsonNode node = new ObjectMapper().valueToTree(transTypeService.queryExercise(EXERCISE_04, o).toString());
        return node;
    }
}
/*


    @ResponseBody
    @GetMapping("/queryExercise_02")
    public JsonNode getExercise02(@RequestParam(value = "Tran_Date", defaultValue = "2021-12-06") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date_tran) throws IOException {
        //輸入指定日期，顯示這一天漁產品的總交易金額跟農產品交易總交易金額大小
        List<Object> res = new ArrayList<>();

        Map<String, Double> map = new HashMap<>();

        int ariCount = 0;
        int fishCount = 0;
        List<TransType> transTypes = transTypeRepository.findMatchByTransDate(date_tran);
        for(TransType transType: transTypes){
            double tempTotal = transType.getTransQuantity() * transType.getAvgPrice();

            if(map.containsKey(transType.getProdType())){
                map.put(transType.getProdType(), tempTotal);
            } else {
                map.put(transType.getProdType(), map.get(transType.getProdType() + tempTotal));
            }

            if(transType.getProdType().equals("農")){
                ariCount++;
            }
            if(transType.getProdType().equals("漁")){
                fishCount++;
            }
        }
        map.put("農", map.get("農")/ariCount);
        map.put("漁", map.get("漁")/fishCount);


        JsonNode node = new ObjectMapper().readTree((JsonParser) map);
        log.info(node.toString());
        return node;
    }
 */