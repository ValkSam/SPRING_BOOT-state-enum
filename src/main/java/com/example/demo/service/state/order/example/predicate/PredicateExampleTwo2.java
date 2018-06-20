package com.example.demo.service.state.order.example.predicate;

import com.example.demo.service.state.order.example.actionparam.ActionParam;
import com.example.demo.service.state.order.example.actionparam.ActionParamTwo;

import java.util.function.Predicate;

/**
 * Created by ValkSam
 */
public class PredicateExampleTwo2 implements Predicate<ActionParam> {
    @Override
    public boolean test(ActionParam param) {
        ActionParamTwo actionParamTwo = (ActionParamTwo) param;
        return actionParamTwo.getSomeParamInteger() == 20;
    }
}
