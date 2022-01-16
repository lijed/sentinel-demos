/*
 * Copyright 2022 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.me.learn.sentineldemos.sentinl;

import com.alibaba.csp.sentinel.init.InitFunc;
import com.me.learn.sentineldemos.SentinelDemosApplication;

/**
 * Description:
 *
 * @Author: Administrator
 * Created: 2022/1/16
 **/
public class FlowRuleInitFunct implements InitFunc {

    @Override
    public void init() throws Exception {
        SentinelDemosApplication.intFlowRule();
    }
}
