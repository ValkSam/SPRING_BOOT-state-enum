package com.example.demo.service.state.order.example.predicate;

import com.example.demo.service.state.order.example.actionparam.ActionParam;
import com.example.demo.service.state.order.example.actionparam.ActionParamTwo;

import java.util.function.Predicate;

import static java.math.BigInteger.TEN;

/**
 * Created by ValkSam
 */
public class PredicateExampleTwo1 implements Predicate<ActionParam> {
    @Override
    public boolean test(ActionParam param) {
        ActionParamTwo actionParamTwo = (ActionParamTwo) param;
        return "VALID_PARAM_VALUE".equals(actionParamTwo.getSomeParamString())
                && (TEN.equals(actionParamTwo.getSomeParamBigInteger()));
    }
}
