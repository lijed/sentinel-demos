package com.me.learn.sentineldemos;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SentinelDemosApplication {

    public static void main(String[] args) {
//        intFlowRule(); // 更改时使用Java的SPI
        SpringApplication.run(SentinelDemosApplication.class, args);
    }

    public static void intFlowRule() {
        List<FlowRule> flowRules = new ArrayList<>();

        FlowRule flowRule = new FlowRule();
        flowRule.setResource("service1");
        flowRule.setStrategy(RuleConstant.STRATEGY_DIRECT);
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule.setCount(1);
        flowRules.add(flowRule);

        FlowRule flowRule1 = new FlowRule();
        flowRule1.setResource("service2");
        flowRule1.setStrategy(RuleConstant.STRATEGY_DIRECT);
        flowRule1.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule1.setCount(1);
        flowRules.add(flowRule1);

        FlowRuleManager.loadRules(flowRules);
    }

}
