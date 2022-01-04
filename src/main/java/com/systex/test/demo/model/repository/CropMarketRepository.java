package com.systex.test.demo.model.repository;

import com.systex.test.demo.model.entity.CropMarket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CropMarketRepository extends CrudRepository<CropMarket, Long> {
}
