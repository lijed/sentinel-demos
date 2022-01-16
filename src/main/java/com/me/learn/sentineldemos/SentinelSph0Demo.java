/*
 * Copyright 2022 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.me.learn.sentineldemos;

import com.alibaba.csp.sentinel.SphO;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @Author: Administrator
 * Created: 2022/1/16
 **/
public class SentinelSph0Demo {

    static {
        intFlowRule();
    }

    public static void main(String[] args) throws InterruptedException {
        while(true) {
            TimeUnit.MILLISECONDS.sleep(50);
            doSomething();
        }
    }

    public static void doSomething() {
        if (SphO.entry("doSomething")) {
            try {
                //业务处理逻辑
                System.out.println("Hello World" + System.currentTimeMillis());
            } finally {
                //资源使用完一定要exit
                SphO.exit();
            }
        } else {
            // 资源访问被限制
            System.out.println("被限流了");
        }
    }

    private static void intFlowRule() {
        List<FlowRule> flowRules = new ArrayList<>();

        FlowRule flowRule = new FlowRule();
        flowRule.setResource("doSomething");
        flowRule.setStrategy(RuleConstant.STRATEGY_DIRECT);
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule.setCount(5);

        flowRules.add(flowRule);

        FlowRuleManager.loadRules(flowRules);
    }
}
