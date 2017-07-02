package com.example.demo.service.state.order;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static com.example.demo.service.state.order.OrderActionEnum.*;
import static com.example.demo.service.state.order.OrderStatusEnum.*;

/**
 * Created by ValkSam
 */
@RunWith(Parameterized.class)
public class OrderStatusEnumAvailableForActionTest {

  private IStatus status;
  private  IStatusAction action;

  public OrderStatusEnumAvailableForActionTest(IStatus status, IStatusAction action) {
    this.status = status;
    this.action = action;
  }

  @Parameterized.Parameters
  public static List<Object[]> isEmptyData() {
    return Arrays.asList(new Object[][]{
        {CREATED, ACCEPT},
        {CREATED, SAVE},
        {CREATED, CREATE_MAIN_SPLITTED},
        {CREATED, CREATE_REST_SPLITTED},
        {CREATED, SPLIT},
        {CREATED, REJECT},
        {ACTIVE, HOLD_FOR_ACCEPTANCE},
        {ACTIVE, SPLIT},
        {ACTIVE, REVOKE},
        {IN_ACCEPTANCE, CREATE_MAIN_SPLITTED},
        {IN_ACCEPTANCE, CREATE_REST_SPLITTED},
        {IN_ACCEPTANCE, ACCEPT},
    });
  }

  @Test
  public void availableForAction() throws Exception {
    Assert.assertTrue(status.availableForAction(action));
  }


}