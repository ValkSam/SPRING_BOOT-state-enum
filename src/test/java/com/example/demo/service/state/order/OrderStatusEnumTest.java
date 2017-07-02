package com.example.demo.service.state.order;

import com.example.demo.service.state.exception.ActionConditionFailedException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static com.example.demo.service.state.order.OrderActionEnum.*;
import static com.example.demo.service.state.order.OrderStatusEnum.*;

/**
 * Created by ValkSam
 */
@RunWith(Theories.class)
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

  @DataPoints
  public static Object[][] nextStateData = new Object[][]{
      {CREATED, SAVE, ACTIVE, IStatusAction.ActionParamVO.builder()
          .currentUserStatus("CONFIRMED")
          .currentUserPermissionList(Arrays.asList("CREATE_ORDER"))
          .build()},
      {CREATED, ACCEPT, SELF_ACCEPTED},
      {CREATED, CREATE_MAIN_SPLITTED, ACTIVE},
      {CREATED, CREATE_REST_SPLITTED, REJECTED},
      {CREATED, SPLIT, SPLITTED},
      {CREATED, REJECT, REJECTED},
  };

  @Theory
  public void nextState(Object[] data) throws Exception {
//    Assume.assumeTrue(data.length == 4);
    IStatus state = (IStatus) data[0];
    IStatusAction action = (IStatusAction) data[1];
    IStatus nextState = (IStatus) data[2];
    IStatusAction.ActionParamVO param = data.length < 4 ? null : (IStatusAction.ActionParamVO) data[3];
    if (param == null) {
      Assert.assertEquals(nextState, state.nextState(action));
    } else {
      Assert.assertEquals(nextState, state.nextState(action, (IStatusAction.ActionParamVO) data[3]));
    }
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
  public void getAvailableForAction() throws Exception {
    Set<IStatusAction> actionList = new HashSet<IStatusAction>() {{
      add(SPLIT);
      add(ACCEPT);
    }};
    Set<IStatus> statusList = new HashSet<IStatus>() {{
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
    Set<IStatus> statusList = new HashSet<IStatus>() {{
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
    Set<IStatus> statusList = new HashSet<IStatus>() {{
      add(IN_ACCEPTANCE);
      add(ACTIVE);
    }};
    Assert.assertArrayEquals(new TreeSet(statusList).toArray(), new TreeSet(IStatus.getMiddleStatesSet(OrderStatusEnum.class)).toArray());
  }

}