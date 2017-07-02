package com.example.demo.service.state.order;

import lombok.Builder;
import lombok.Getter;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by ValkSam
 */
public enum OrderActionEnum implements IStatusAction{
  ACCEPT,
  SAVE{{
    getPredicates().add(new SavePredicate());
  }},
  REJECT,
  REVOKE,
  SPLIT,
  CREATE_MAIN_SPLITTED,
  CREATE_REST_SPLITTED,
  HOLD_FOR_ACCEPTANCE;

  private final List<Predicate<ActionParamVO>> predicates = new ArrayList<>();

  @Override
  public List<Predicate<ActionParamVO>> getPredicates() {
    return predicates;
  }

  public static class SavePredicate implements Predicate<ActionParamVO>{
    @Override
    public boolean test(ActionParamVO param) {
      return "CONFIRMED".equals(param.getCurrentUserStatus())
          && (param.getCurrentUserPermissionList() != null && param.getCurrentUserPermissionList().contains("CREATE_ORDER"));
    }
  }


}
