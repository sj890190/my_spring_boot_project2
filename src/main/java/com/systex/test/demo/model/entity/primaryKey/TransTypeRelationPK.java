package com.systex.test.demo.model.entity.primaryKey;

import lombok.Data;

import java.io.Serializable;

@Data
public class TransTypeRelationPK implements Serializable {
    //將資料表的部分欄位組合為複合鍵
    private String transDate;// string 交易日期
    private String prodCode;// string 農產品代碼
    private String prodName;// string 農產品名稱
    private String marketCode;// string 市場代號
    private String marketName;// string 市場名稱
}
