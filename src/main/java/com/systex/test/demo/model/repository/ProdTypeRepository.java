package com.systex.test.demo.model.repository;

import com.systex.test.demo.model.entity.ProdType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdTypeRepository extends CrudRepository<ProdType, String> {
    Iterable<ProdType> findAllByProdType(String prodType);
}
