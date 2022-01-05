package com.systex.test.demo.model.repository;

import com.systex.test.demo.model.entity.AgriProductsTransType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgriProductsTransTypeRepository extends CrudRepository<AgriProductsTransType, String> {
    AgriProductsTransType findTopByOrderByTranDateDesc();
}
