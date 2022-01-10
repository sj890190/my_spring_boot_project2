package com.systex.test.demo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.systex.test.demo.model.service.TransTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static com.systex.test.demo.config.DateConverter.DATE_TO_STRING;
import static com.systex.test.demo.config.DateConverter.LOCALDATE_TO_DATE;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/TransType")
public class TransTypeController {

    @Autowired
    TransTypeService transTypeService;

    private final static String EXERCISE_01 = "SELECT MIN(trans_quantity) AS minQty, MAX(trans_quantity) AS maxQty, AVG(trans_quantity) AS avgQty" +
            " FROM trans_type WHERE tran_date BETWEEN ? AND ? AND prod_type = ? AND prod_name = ? AND market_name = ? GROUP BY prod_name";

    private final static String EXERCISE_02 = "SELECT prod_type, SUM(avg_price * trans_quantity) AS total_price" +
            " FROM trans_type WHERE tran_date = ? GROUP BY prod_type";

    private final static String EXERCISE_03 = "SELECT tran_date, market_name, prod_name, trans_quantity" +
            " FROM trans_type WHERE tran_date BETWEEN ? AND ? AND market_name = ? AND prod_name = ? AND prod_type = ? ORDER BY tran_date";

    @ResponseBody
    @GetMapping("/queryExercise_01")
    //輸入起始時間跟結束時間，顯示這段時間農產品中的"椰子"在"台北二"這個市場中的平均交易量、最大交易量、最低交易量
    public JsonNode getExercise01(@RequestParam(value = "Start_Date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date_start,
                                  @RequestParam(value = "End_Date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date_end) throws Exception {
        //轉成Date
        log.info("Exercise_01 Start==================================================");
        log.info("LocalDate start_date: {}", date_start);
        log.info("LocalDate date_end: {}", date_end);

        Date start_date = LOCALDATE_TO_DATE(date_start);
        log.info("Date start_date: {}", start_date);
        Date end_date = LOCALDATE_TO_DATE(date_end);
        log.info("Date date_end: {}", date_end);

        //參數設定
        String type = "農";
        String prodName = "椰子";
        String marketName = "台北二";
        Map params = new HashMap<>();
        params.put("start_date",DATE_TO_STRING(start_date, type));
        params.put("end_date",DATE_TO_STRING(end_date, type));
        //檢查有無資料
        transTypeService.checkDataStatus(type, start_date, end_date);
        transTypeService.setApiUrl(type, params, new LinkedList<>(), new LinkedList<>());

        //轉成Entity
        Object[] o = {date_start, date_end, type, prodName, marketName};
        JsonNode node = new ObjectMapper().valueToTree(transTypeService.queryExercise(EXERCISE_01, o).toString());
        log.info(node.toString());
        log.info("Exercise_01 End==================================================");
        return node;
    }

    @ResponseBody
    @GetMapping(value = "/queryExercise_02")
    //輸入指定日期，顯示這一天漁產品的總交易金額跟農產品交易總交易金額大小
    public JsonNode getExercise02(@RequestParam(value = "Start_Date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date_start) throws Exception {
        //轉成Date
        Date start_date = LOCALDATE_TO_DATE(date_start);
        log.info("start_date: {}", start_date);

        String type = "漁";
        Map params = new HashMap<>();
        params.put("start_date",DATE_TO_STRING(start_date, type));
        params.put("end_date",DATE_TO_STRING(start_date, type));
        transTypeService.checkDataStatus(type, start_date, start_date);
        transTypeService.setApiUrl(type, params, new LinkedList<>(), new LinkedList<>());
        Object[] o = {start_date};

        JsonNode node = new ObjectMapper().valueToTree(transTypeService.queryExercise(EXERCISE_02, o).toString());
        log.info(node.toString());
        return node;
    }

    @ResponseBody
    @GetMapping(value = "/queryExercise_03")
    //輸入指定日期跟市場與農產品，顯示過去五天的交易量是否為"嚴格遞增"
    public JsonNode getExercise03(@RequestParam(value = "Start_Date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date_tran,
                                  @RequestParam(value = "Market_Name") String market_name,
                                  @RequestParam(value = "Prod_Name") String prod_name) throws Exception {
        LocalDate start_date = date_tran.minusDays(5);
        LocalDate end_date = start_date.plusDays(4);
        Object[] o = {start_date, end_date, market_name, prod_name, "農"};

        boolean isTrue = false;
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
            isTrue = true;
        }

        //依據Flag判斷是否為嚴格遞增
        String jsonStr = "";
        if (isTrue) {
            jsonStr = "{\"嚴格遞增\":\"是\"}";
        } else {
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
    //輸入指定日期跟市場與農產品，顯示過去五天的交易量是否為"嚴格遞增"
    public JsonNode getExercise04(@RequestParam(value = "Tran_Date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date_tran) {

        LocalDate start_date = date_tran.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate end_date = date_tran.with(TemporalAdjusters.lastDayOfMonth());
        log.info("start_date: {}, end_date: {}", start_date, end_date);
        Object[] o = {start_date, end_date, "農"};
        JsonNode node = new ObjectMapper().valueToTree(transTypeService.queryExercise(EXERCISE_04, o).toString());
        return node;
    }

}