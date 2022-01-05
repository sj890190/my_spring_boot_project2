package com.systex.test.demo.model.repository;

import com.systex.test.demo.model.entity.AgriFisheryProductsTransTypeType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgriFisheryProductsTransTypeTypeRepository extends CrudRepository<AgriFisheryProductsTransTypeType, String> {
    AgriFisheryProductsTransTypeType findTopByOrderByTranDateDesc();
}
