package com.example.demo.service.state.order.example;

import com.example.demo.service.state.order.IStatusAction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by ValkSam
 */
public enum ExampleActionEnum implements IStatusAction {
  ACTION_1,
  ACTION_2 {{
    getPredicates().add(new PredicateExample());
  }},
  ACTION_3,
  ACTION_4,
  ACTION_5,
  ACTION_6,
  ACTION_7,
  ACTION_8;

  private final List<Predicate<ActionParamVO>> predicates = new ArrayList<>();

  @Override
  public List<Predicate<ActionParamVO>> getPredicates() {
    return predicates;
  }

  public static class PredicateExample implements Predicate<ActionParamVO>{
    @Override
    public boolean test(ActionParamVO param) {
      return "VALID_PARAM_VALUE".equals(param.getSomeParam())
          && (param.getSomePermissionList() != null && param.getSomePermissionList().contains("SOME_VALID_PERMISSION"));
    }
  }


}
