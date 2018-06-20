package com.example.demo.service.state.order.example;

import com.example.demo.service.state.order.IStatusAction;
import com.example.demo.service.state.order.example.actionparam.ActionParam;
import com.example.demo.service.state.order.example.actionparam.ActionParamTwo;
import com.example.demo.service.state.order.example.predicate.PredicateExampleOne;
import com.example.demo.service.state.order.example.predicate.PredicateExampleTwo1;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by ValkSam
 */
public enum ExampleActionEnum implements IStatusAction<ActionParam> {
    ACTION_1,
    ACTION_2 {{
        getPredicates().add(new PredicateExampleOne());
    }},
    ACTION_3,
    ACTION_4,
    ACTION_5 {{
        getPredicates().add(new PredicateExampleTwo1());
        getPredicates().add((param) -> {
            ActionParamTwo actionParamTwo = (ActionParamTwo) param;
            return actionParamTwo.getSomeParamInteger() == 20;
        });
    }},
    ACTION_6,
    ACTION_7,
    ACTION_8;

    private final List<Predicate<ActionParam>> predicates = new ArrayList<>();

    @Override
    public List<Predicate<ActionParam>> getPredicates() {
        return predicates;
    }

}
