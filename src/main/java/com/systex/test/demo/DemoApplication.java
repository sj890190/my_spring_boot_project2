package com.systex.test.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@CrossOrigin
@Controller
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@GetMapping("/index")
	public String home(){
		return "index";
	}

	//將Json集合至Map後, 轉入Entity
	public static <T> T setMapToEntity(Map map, int index, Class<T> clazz){
		T t = null;
		try {
			t = new ObjectMapper().readValue(map.get(index).toString(), clazz);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return t;
	}

	//設置Url, 並Json取回的資料
	public static Map setUrlToJson(String url, Map<String, String> params) throws JSONException {
		String jsonStr = "";
		RestTemplate restTemplate = new RestTemplate();
		if(!params.isEmpty()){
			jsonStr = restTemplate.getForEntity(url, String.class, params).getBody();
		}else{
			jsonStr = restTemplate.getForEntity(url, String.class).getBody();
		}
		Map res = new HashMap();
		if(jsonStr != null){
			res = checkJsonToMap(jsonStr); // 檢查Json存入Map
		}
		return res;
	}

	//檢查Json字串並回傳Map
	public static Map checkJsonToMap(String jsonStr) throws JSONException {

		Map map = new HashMap<>();

		JSONObject parentObject = new JSONObject(jsonStr);

		// 狀態不是OK時, 回傳map為取得的狀態
		if(!parentObject.get("RS").equals("OK")){
			map.put("RS", parentObject.get("RS"));
			log.info("object.get(\"RS\"): {}, map: {}", parentObject.get("RS"), map);
			return map;
		}
		// 分頁有true時, 回傳map為取得的分頁狀態
		if(!parentObject.get("Next").equals(false)){
			map.put("Next", parentObject.get("Next"));
			log.info("object.get(\"Next\"): {}", parentObject.get("Next"));
			return map;
		}
		// RS狀態為OK 且 沒有分頁, 但沒有Data
		if(parentObject.get("Data").equals("")){
			map.put("Data", parentObject.get("Data"));
			log.info("object.get(\"Data\"): {}, map: {}", parentObject.get("Data"), map);
			return map;
		}

		// RS狀態為OK 且 沒有分頁, 但有Data
		JSONArray dataArray = parentObject.getJSONArray("Data");
		for(int i=0; i<dataArray.length(); i++){
			map.put(i, dataArray.get(i));
			//log.info("i: {}, dataArray.get(i): {}", i, dataArray.get(i));
		}
		return map;
	}
}
