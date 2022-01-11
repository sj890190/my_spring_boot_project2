package com.systex.test.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.systex.test.demo.model.service.TransTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.*;

import static com.systex.test.demo.config.DateConverter.DATE_TO_STRING;
import static com.systex.test.demo.config.DateConverter.LOCALDATE_TO_DATE;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/TransType")
@Api(tags = "交易資訊")
public class TransTypeController {

    @Autowired
    TransTypeService transTypeService;

    private final static String EXERCISE_01 = "SELECT MIN(trans_quantity) AS minQty, MAX(trans_quantity) AS maxQty, AVG(trans_quantity) AS avgQty" +
            " FROM trans_type WHERE tran_date BETWEEN ? AND ? AND prod_type = ? AND prod_name = ? AND market_name = ? GROUP BY prod_name";

    private final static String EXERCISE_02 = "SELECT prod_type, SUM(avg_price * trans_quantity) AS total_price" +
            " FROM trans_type WHERE tran_date = ? GROUP BY prod_type";

    private final static String EXERCISE_03 = "SELECT tran_date, market_name, prod_name, trans_quantity" +
            " FROM trans_type WHERE tran_date BETWEEN ? AND ? AND market_name = ? AND prod_name = ? AND prod_type = ? ORDER BY tran_date ASC";

    private final static String EXERCISE_04 = "SELECT prod_name, SUM(trans_quantity) totalQty" +
            " FROM uv_tran_data WHERE tran_year = ? AND tran_month = ? AND prod_type = ?" +
            " GROUP BY prod_name ORDER BY SUM(trans_quantity) DESC LIMIT 10";

    @ResponseBody
    @GetMapping(value = "/queryExercise_01", produces = "text/html;charset=UTF-8")
    @ApiOperation("顯示區間內, 農產品中的「椰子」在「台北二」這個市場中的平均交易量、最大交易量、最低交易量")
    //輸入起始時間跟結束時間，
    public String getExercise01(@ApiParam(value = "起始時間", required = true) @RequestParam(value = "Start_Date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date_start,
                                  @ApiParam(value = "結束時間", required = true) @RequestParam(value = "End_Date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date_end) throws Exception {
        log.info("Exercise_01 Start==================================================");
        //轉成Date
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
        log.info("Exercise_01 End====================================================");
        return node.toString();
    }

    @ResponseBody
    @GetMapping(value = "/queryExercise_02", produces = "text/html;charset=UTF-8")
    @ApiOperation("指定日期中, 漁產品的總交易金額跟農產品交易總交易金額大小")
    public String getExercise02(@ApiParam(value = "指定時間", required = true) @RequestParam(value = "Start_Date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date_start) throws Exception {
        log.info("Exercise_02 Start==================================================");
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
        log.info("Exercise_02 End====================================================");
        return node.toString();
    }

    @ResponseBody
    @GetMapping(value = "/queryExercise_03", produces = "text/html;charset=UTF-8")
    @ApiOperation("顯示過去五天內, 市場與農產品的交易量是否為「嚴格遞增」")
    public String getExercise03(@ApiParam(value = "指定日期", required = true) @RequestParam(value = "Start_Date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date_tran,
                                  @ApiParam(value = "市場名稱", required = true) @RequestParam(value = "Market_Name") String market_name,
                                  @ApiParam(value = "產品名稱", required = true) @RequestParam(value = "Prod_Name") String prod_name) throws Exception {
        log.info("Exercise_03 Start==================================================");
        LocalDate start_date = date_tran.minusDays(5);
        LocalDate end_date = start_date.plusDays(4);
        log.info("start_date: {}", start_date);
        log.info("end_date: {}", end_date);
        Object[] o = {start_date, end_date, market_name, prod_name, "農"};

        boolean isTrue = true;
        //存放數量, 用來比較是否為嚴格遞增
        Queue<Integer> queue = new LinkedList();
        //取得查詢結果的List<Map>
        List<Map<String, Object>> list = transTypeService.queryExercise(EXERCISE_03, o);
        for (int i = 0; i < list.size(); i++) {
            //取得SQL資料存為Map
            Map<String, Object> map = list.get(i);
            //以field名稱找尋對應的value
            int trans_quantity = Integer.parseInt(map.get("trans_quantity").toString());
            //若Queue為空, 將Map取得的數量存入並繼續下一個
            if (queue.isEmpty()) {
                queue.add(trans_quantity);
                continue;
            }
            //因為是嚴格遞增, 所以只能用小於, 出現大於等於就返回為非嚴格遞增, 否則將數量存入queue
            if (queue.poll() < trans_quantity) {
                queue.add(trans_quantity);
            }else{
                isTrue = false;
                break;
            }
        }

        //依據Flag判斷是否為嚴格遞增
        JSONObject jsonObject = new JSONObject();
        if (isTrue) {
            jsonObject.put("嚴格遞增","是");
        } else {
            jsonObject.put("嚴格遞增","否");
        }
        JsonNode node = new ObjectMapper().valueToTree(jsonObject.toString());
        /*
         * 轉成JsonArray
         * List<Map<String, String>> t = new LinkedList<>();
         * Map m = new HashMap<>();
         * if (isTrue) {
         *   m.put("嚴格遞增","是");
         * } else {
         *   m.put("嚴格遞增","否");
         * }
         * t.add(m);
         * JsonNode node = new ObjectMapper().valueToTree(t.toString());
         */
        log.info("Exercise_03 End====================================================");
        return node.toString();
    }

    @ResponseBody
    @GetMapping(value = "/queryExercise_04", produces = "text/html;charset=UTF-8")
    @ApiOperation("顯示指定年月中, 前10名最暢銷（總交易量）的農產品（不分市場）")
    public String getExercise04(@ApiParam(value = "指定年份", example = "1970") @RequestParam(value = "Trans_Year") @Min(value = 1970)  int tran_year,
                                @ApiParam(value = "指定年份", example = "1") @RequestParam(value = "Trans_Month") @Min(value = 1) @Max(value = 12) int tran_month) throws JsonProcessingException {

        //public String getExercise04(@ApiParam(name = "指定年份") @RequestParam(value = "Trans_Year") @Min(value = 1970)  int tran_year,
    //                            @ApiParam(name = "指定月份") @RequestParam(value = "Trans_Month") @Min(value = 1) @Max(value = 12) int tran_month) throws JsonProcessingException {
        log.info("Exercise_04 Start==================================================");
        log.info("tran_year: {}", tran_year);
        log.info("tran_month: {}", tran_month);
        Object[] o = {tran_year, tran_month, "農"};
        JsonNode node = new ObjectMapper().valueToTree(transTypeService.queryExercise(EXERCISE_04, o).toString());
        log.info("Exercise_04 End====================================================");
        //return node;
        //JsonNode -> Json
        return new ObjectMapper().writeValueAsString(node);
    }

}