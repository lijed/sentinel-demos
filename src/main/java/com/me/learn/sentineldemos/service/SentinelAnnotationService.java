/*
 * Copyright 2022 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.me.learn.sentineldemos.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.me.learn.sentineldemos.commons.ExceptionUtils;
import org.springframework.stereotype.Service;

/**
 * Description:
 *
 * @Author: Administrator
 * Created: 2022/1/16
 **/
@Service
public class SentinelAnnotationService {


    /**
     *
     * 注意异常处理方式是静态方法
     * @return
     */
    @SentinelResource(value = "service1", blockHandler = "handleException", blockHandlerClass = ExceptionUtils.class)
    public String service1() {
        System.out.println("service1 success");
        return "service01 return success";
    }


//    @SentinelResource(value = "service2", fallback = "fallback")
    @SentinelResource(value = "service2", fallback = "fallback", fallbackClass = FallBackService.class)
    public String service2() {
        System.out.println("service2 success");
        return "service2 return success";
    }

    public String fallback() {
        return "系统繁忙，稍后再试";
    }
}
