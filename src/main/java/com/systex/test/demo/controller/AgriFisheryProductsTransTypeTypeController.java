package com.systex.test.demo.controller;

import com.systex.test.demo.model.repository.CropMarketRepository;
import com.systex.test.demo.model.repository.CropTypeRepository;
import com.systex.test.demo.model.service.AgriFisheryProductsTransTypeTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/agriFisheryProductsTransTypeTypeController")
public class AgriFisheryProductsTransTypeTypeController {
    @Autowired
    AgriFisheryProductsTransTypeTypeService AgriFisheryProductsTransTypeTypeService;

    @Autowired
    CropTypeRepository cropTypeRepository;
    @Autowired
    CropMarketRepository cropMarketRepository;
}
