/*
 * Copyright 2022 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.me.learn.sentineldemos;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
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
public class SentinelSphUDemo {
    public static void main(String[] args) throws InterruptedException {
        intFlowRule();

        while (true) {
            doSomething();
        }
    }

    /**
     *  每秒的QPS是5，如果超过5，将抛出 FlowException
     */
    private static void doSomething() {
        try (Entry doSomething = SphU.entry("doSomething")) {
            TimeUnit.MILLISECONDS.sleep(150);
            System.out.println("do some process");
        } catch (BlockException | InterruptedException ex) {
            ex.printStackTrace();
            System.out.println("被限流了");
        }
    }

    private static void intFlowRule() {
        List<FlowRule> flowRules = new ArrayList<FlowRule>();

        FlowRule flowRule = new FlowRule();
        flowRule.setResource("doSomething");
        flowRule.setStrategy(RuleConstant.STRATEGY_DIRECT);
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule.setCount(5);

        flowRules.add(flowRule);

        FlowRuleManager.loadRules(flowRules);
    }
}
