package com.systex.test.demo.model.repository;

import com.systex.test.demo.model.entity.MarketType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface MarketTypeRepository extends CrudRepository<MarketType, String> {
}
