package com.systex.test.demo.model.entity.primaryKey;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProdTypeRelationPK implements Serializable {
    //將資料表的部分欄位組合為複合鍵
    String prodType;// 產品類型 string
    String prodCode;// 產品代碼 string
    String prodName;// 產品名稱 string
}
