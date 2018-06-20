package com.example.demo.service.state.order.example.predicate;

import com.example.demo.service.state.order.example.actionparam.ActionParam;
import com.example.demo.service.state.order.example.actionparam.ActionParamOne;

import java.util.function.Predicate;

/**
 * Created by ValkSam
 */
public class PredicateExampleOne implements Predicate<ActionParam> {
    @Override
    public boolean test(ActionParam param) {
        ActionParamOne actionParamOne = (ActionParamOne) param;
        return "VALID_PARAM_VALUE".equals(actionParamOne.getSomeParam())
                && (actionParamOne.getSomePermissionList() != null && actionParamOne.getSomePermissionList().contains("SOME_VALID_PERMISSION"));
    }
}
