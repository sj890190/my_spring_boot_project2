package com.systex.test.demo.model.repository;

import com.systex.test.demo.model.entity.TransType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TransTypeRepository extends CrudRepository<TransType, String> {
    TransType findTopByOrderByTransDateDesc();
    //long countByTransDate(Date tran_date);
    long countByTransDate(String transDate);
    //long countByTransDateGreaterThanEqualTransDateLessThanEqualAnd(Date endDate, Date startDate);
}