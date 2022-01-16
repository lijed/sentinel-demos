/*
 * Copyright 2022 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.me.learn.sentineldemos.controller;

import com.me.learn.sentineldemos.service.SentinelAnnotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @Author: Administrator
 * Created: 2022/1/16
 **/

@RestController
public class SentinelAnnotationController {


    @Autowired
    private SentinelAnnotationService sentinelService;


    @GetMapping("/sentinel1")
    public String sentinelAnnotation1() {
        return sentinelService.service1();
    }
    @GetMapping("/fallback")
    public String sentinelAnnotation2() {
        return sentinelService.service2();
    }
}
