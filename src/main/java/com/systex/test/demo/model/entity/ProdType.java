package com.systex.test.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.systex.test.demo.model.entity.primaryKey.ProdTypeRelationPK;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Table(name = "prod_type")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ProdTypeRelationPK.class)
public class ProdType {
    @Id
    @Column(length = 1)
    @JsonIgnore
    String prodType;// 農產品代碼 string
    @Id
    @Column(length = 10)
    @JsonProperty(value = "CropCode")
    String prodCode;// 農產品代碼 string
    @Id
    @Column(length = 100)
    @JsonProperty(value = "CropName")
    String prodName;// 農產品名稱 string

    @JsonSetter("SeafoodProdCode")
    public void setSeafoodProdCode(String seafoodProdCode){
        this.prodCode = seafoodProdCode;
    }
    @JsonSetter("SeafoodProdName")
    public void setSeafoodProdName(String seafoodProdName){
        this.prodName = seafoodProdName;
    }
}
