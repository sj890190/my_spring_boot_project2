package com.systex.test.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Table(name = "cropType")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CropType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(length = 100)
    @JsonProperty(value = "CropName")
    String cropName;// 農產品名稱 string
    @Column(length = 10)
    @JsonProperty(value = "CropCode")
    String cropCode;// 農產品代碼 string
}
