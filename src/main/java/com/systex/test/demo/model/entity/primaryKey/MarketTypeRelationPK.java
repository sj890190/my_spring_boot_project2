package com.systex.test.demo.model.entity.primaryKey;

import lombok.Data;

import java.io.Serializable;

@Data
public class MarketTypeRelationPK implements Serializable {
    //將資料表的部分欄位組合為複合鍵
    String marketType;// 市場類型 string
    String marketCode;// 市場代碼 string
    String marketName;// 市場名稱 string
}
