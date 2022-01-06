package com.systex.test.demo.model.repository;

import com.systex.test.demo.model.entity.TransType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransTypeRepository extends CrudRepository<TransType, String> {
    TransType findTopByOrderByTransDateDesc();
    List<TransType> findMatchByTransDate(Date tran_date);
}