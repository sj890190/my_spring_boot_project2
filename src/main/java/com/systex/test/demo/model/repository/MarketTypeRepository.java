package com.systex.test.demo.model.repository;

import com.systex.test.demo.model.entity.MarketType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketTypeRepository extends CrudRepository<MarketType, String> {
    MarketType findTopByMarketName(String marketName);
    Iterable<MarketType> findAllByMarketType(String marketType);
}
