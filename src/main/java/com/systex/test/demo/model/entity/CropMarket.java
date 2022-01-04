package com.systex.test.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Table(name = "cropMarket")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CropMarket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(length = 100)
    @JsonProperty(value = "MarketName")
    String marketName;// 市場名稱 string
    @Column(length = 10)
    @JsonProperty(value = "MarketCode")
    String marketCode;// 市場品代碼 string
}
