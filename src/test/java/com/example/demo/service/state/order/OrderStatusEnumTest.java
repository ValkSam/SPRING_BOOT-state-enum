package com.example.demo.service.state.order;

import com.example.demo.service.state.exception.ActionConditionFailedException;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static com.example.demo.service.state.order.OrderActionEnum.*;
import static com.example.demo.service.state.order.OrderStatusEnum.*;

/**
 * Created by ValkSam on 01.07.2017.
 */
public class OrderStatusEnumTest {
  @Test
  public void collectAllSchemaMapNodesSet() throws Exception {
    Set<IStatus> fullList = new TreeSet<>(IStatus.collectAllSchemaMapNodesSet(OrderStatusEnum.class));
    Set<IStatus> collectedFullList = new TreeSet<>();
    collectedFullList.addAll(IStatus.getMiddleStatesSet(OrderStatusEnum.class));
    collectedFullList.addAll(IStatus.getEndStatesSet(OrderStatusEnum.class));
    System.out.println(collectedFullList);
    Assert.assertArrayEquals(fullList.toArray(), collectedFullList.toArray());
  }

  @Test
  public void nextState() throws Exception {
    OrderStatusEnum state = CREATED;
    OrderStatusEnum nextState = ACTIVE;
    IStatusAction.ActionParamVO param = IStatusAction.ActionParamVO.builder()
        .currentUserStatus("CONFIRMED")
        .currentUserPermissionList(Arrays.asList("CREATE_ORDER"))
        .build();
    Assert.assertEquals(nextState, state.nextState(SAVE, param));
  }

  @Test(expected = ActionConditionFailedException.class)
  public void nextStateFail() throws Exception {
    OrderStatusEnum state = CREATED;
    OrderStatusEnum nextState = ACTIVE;
    IStatusAction.ActionParamVO param = IStatusAction.ActionParamVO.builder()
        .currentUserStatus("REGISTERED")
        .currentUserPermissionList(Arrays.asList("CREATE_ORDER"))
        .build();
    Assert.assertEquals(nextState, state.nextState(SAVE, param));
  }

  @Test
  public void availableForAction() throws Exception {
    Assert.assertTrue(CREATED.availableForAction(ACCEPT));
    Assert.assertTrue(CREATED.availableForAction(SAVE));
    Assert.assertTrue(CREATED.availableForAction(CREATE_MAIN_SPLITTED));
    Assert.assertTrue(CREATED.availableForAction(CREATE_REST_SPLITTED));
    Assert.assertTrue(CREATED.availableForAction(SPLIT));
    Assert.assertTrue(CREATED.availableForAction(REJECT));
    /**/
    Assert.assertFalse(CREATED.availableForAction(REVOKE));
    Assert.assertFalse(CREATED.availableForAction(HOLD_FOR_ACCEPTANCE));
  }

  @Test
  public void getAvailableForAction() throws Exception {
    Set<IStatusAction> actionList = new HashSet<IStatusAction>(){{
      add(SPLIT);
      add(ACCEPT);
    }};
    Set<IStatus> statusList = new HashSet<IStatus>(){{
      add(CREATED);
      add(ACTIVE);
      add(IN_ACCEPTANCE);
    }};
    Assert.assertArrayEquals(statusList.toArray(), IStatus.getAvailableForAction(OrderStatusEnum.class, actionList).toArray());
  }

  @Test
  public void getBeginState() throws Exception {
    OrderStatusEnum state = CREATED;
    Assert.assertEquals(state, IStatus.getBeginState(OrderStatusEnum.class));
  }

  @Test
  public void getEndStatesSet() throws Exception {
    Set<IStatus> statusList = new HashSet<IStatus>(){{
      add(SELF_ACCEPTED);
      add(ACCEPTED);
      add(REJECTED);
      add(REVOKED);
      add(SPLITTED);
    }};
    Assert.assertArrayEquals(new TreeSet(statusList).toArray(), new TreeSet(IStatus.getEndStatesSet(OrderStatusEnum.class)).toArray());
  }

  @Test
  public void getMiddleStatesSet() throws Exception {
    System.out.println(IStatus.getMiddleStatesSet(OrderStatusEnum.class));
    Set<IStatus> statusList = new HashSet<IStatus>(){{
      add(IN_ACCEPTANCE);
      add(ACTIVE);
    }};
    Assert.assertArrayEquals(new TreeSet(statusList).toArray(), new TreeSet(IStatus.getMiddleStatesSet(OrderStatusEnum.class)).toArray());
  }

}