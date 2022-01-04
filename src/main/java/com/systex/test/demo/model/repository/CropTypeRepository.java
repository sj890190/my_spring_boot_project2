package com.systex.test.demo.model.repository;

import com.systex.test.demo.model.entity.CropType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CropTypeRepository extends CrudRepository<CropType, Long> {
}
