package com.systex.test.demo.model.repository;

import com.systex.test.demo.model.entity.TransType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransTypeRepository extends CrudRepository<TransType, String> {
    TransType findTopByOrderByTransDateDesc();
}
