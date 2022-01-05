package com.systex.test.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.systex.test.demo.model.entity.primaryKey.MarketTypeRelationPK;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Table(name = "market_type")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@IdClass(MarketTypeRelationPK.class)
public class MarketType {
    @Id
    @Column(length = 1)
    @JsonIgnore
    String marketType;// 市場類型 string
    @Id
    @Column(length = 10)
    @JsonProperty(value = "MarketCode")
    String marketCode;// 市場代碼 string
    @Id
    @Column(length = 100)
    @JsonProperty(value = "MarketName")
    String marketName;// 市場名稱 string

    @JsonSetter("SeafoodMarketCode")
    public void setSeafoodMarketCode(String seafoodMarketCode) {
        this.marketCode = seafoodMarketCode;
    }
    @JsonSetter("SeafoodMarketName")
    public void setSeafoodMarketName(String seafoodMarketName){
        this.marketName = seafoodMarketName;
    }
}
