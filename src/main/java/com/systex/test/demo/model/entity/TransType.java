package com.systex.test.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.systex.test.demo.model.entity.primaryKey.TransTypeRelationPK;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.ParseException;
import java.util.Date;

import static com.systex.test.demo.config.DateConverter.STRING_TO_DATE;

@Slf4j
@Data
@Table(name = "trans_type")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@IdClass(TransTypeRelationPK.class)
public class TransType {
    @Id
    @Column(length = 1)
    @JsonIgnore
    private String prodType;// string 產品類型
    @Id
    @Column(length = 10)
    @JsonProperty(value = "TransDate")
    private String transDate;// string 交易日期
    @Id
    @Column(length = 10)
    @JsonProperty(value = "ProdCode")
    private String prodCode;// string 產品代碼
    @Id
    @Column(length = 100)
    @JsonProperty(value = "ProdName")
    private String prodName;// string 產品名稱
    @Id
    @Column(length = 10)
    @JsonProperty(value = "MarketCode")
    private String marketCode;// string 市場代號
    @Column(length = 100)
    @JsonProperty(value = "MarketName")
    private String marketName;// string 市場名稱
    @Column(precision = 10, scale = 2)
    @JsonProperty(value = "Upper_Price")
    private Float upperPrice;// number 上價(元/公斤)
    @Column(precision = 10, scale = 2)
    @JsonProperty(value = "Middle_Price")
    private Float middlePrice;// number 中價(元/公斤)
    @Column(precision = 10, scale = 2)
    @JsonProperty(value = "Lower_Price")
    private Float lowerPrice;// number 下價(元/公斤)
    @Column(precision = 10, scale = 2)
    @JsonProperty(value = "Avg_Price")
    private Float avgPrice;// number 平均價(元/公斤)
    @JsonProperty(value = "Trans_Quantity")
    private Long transQuantity;// number 交易量(公斤)
    @Column(length = 10)
    @JsonIgnore
    @DateTimeFormat(pattern = "yyyy-MM-dd T HH:mm:ss")
    //@ApiModelProperty(value = "交易日期(西元)", required = true, dataType = "java.time.LocalDate")
    private Date tranDate;// string 交易日期

    public void setTranDate(String date) throws ParseException {
        this.tranDate = STRING_TO_DATE(date);
    }

    @JsonSetter("CropCode")
    public void setCropCode(String cropCode){ this.prodCode = cropCode; }
    @JsonSetter("CropName")
    public void setCropName(String cropName){ this.prodName = cropName; }
    @JsonSetter("Category")
    public void setCategory(String category){
        this.prodType = category;
    }
    @JsonSetter("SeafoodProdCode")
    public void setSeafoodProdCode(String seafoodProdCode){
        this.prodCode = seafoodProdCode;
    }
    @JsonSetter("SeafoodProdName")
    public void setSeafoodProdName(String seafoodProdName){
        this.prodName = seafoodProdName;
    }
    @JsonSetter("SeafoodMarketCode")
    public void setSeafoodMarketCode(String seafoodMarketCode){
        this.marketCode = seafoodMarketCode;
    }
    @JsonSetter("SeafoodMarketName")
    public void setSeafoodMarketName(String seafoodMarketName){
        this.marketCode = seafoodMarketName;
    }
}
