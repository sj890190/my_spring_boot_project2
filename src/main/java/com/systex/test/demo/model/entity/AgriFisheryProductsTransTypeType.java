package com.systex.test.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Data
@Table(name = "agri_fishery_products_trans_type_type")
@Entity
@IdClass(AgriFisheryProductsTransTypeTypeRelationPK.class)
@NoArgsConstructor
@AllArgsConstructor
public class AgriFisheryProductsTransTypeType {
    @Id
    @Column(length = 7)
    @JsonProperty(value = "TransDate")
    private String transDate;// string 交易日期
    @Id
    @Column(length = 10)
    @JsonProperty(value = "ProdCode")
    private String prodCode;// string 農產品代碼
    @Id
    @Column(length = 100)
    @JsonProperty(value = "ProdName")
    private String prodName;// string 農產品名稱
    @Id
    @Column(length = 10)
    @JsonProperty(value = "MarketCode")
    private String marketCode;// string 市場代號
    @Id
    @Column(length = 100)
    @JsonProperty(value = "MarketName")
    private String marketName;// string 市場名稱
    @Column(length = 1)
    @JsonProperty(value = "Category")
    private String category;// string 產業行別
    @Column(precision = 5, scale = 2)
    @JsonProperty(value = "Upper_Price")
    private Float upperPrice;// number 上價(元/公斤)
    @Column(precision = 5, scale = 2)
    @JsonProperty(value = "Middle_Price")
    private Float middlePrice;// number 中價(元/公斤)
    @Column(precision = 5, scale = 2)
    @JsonProperty(value = "Lower_Price")
    private Float lowerPrice;// number 下價(元/公斤)
    @Column(precision = 5, scale = 2)
    @JsonProperty(value = "Avg_Price")
    private Float avgPrice;// number 平均價(元/公斤)
    @JsonProperty(value = "Trans_Quantity")
    private Long transQuantity;// number 交易量(公斤)
    @DateTimeFormat(pattern = "yyyy-MM-dd T HH:mm:ss")
    @Column(name = "tran_Date")
    private Date tranDate;

    public void setTranDate(String d) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String day = d.substring(d.length() - 2);
        d = d.substring(0, d.length() - day.length());
        String month = d.substring(d.length() - 2);
        d = d.substring(0, d.length() - month.length());
        String year = String.valueOf((Integer.parseInt(d) + 1911));
        String date = year + "-" + month + "-" + day;
        this.tranDate = sdf.parse(date);;
    }
}
