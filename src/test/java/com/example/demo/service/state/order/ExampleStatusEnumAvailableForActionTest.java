package com.example.demo.service.state.order;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static com.example.demo.service.state.order.example.ExampleActionEnum.*;
import static com.example.demo.service.state.order.example.ExampleStatusEnum.*;

/**
 * Created by ValkSam
 */
@RunWith(Parameterized.class)
public class ExampleStatusEnumAvailableForActionTest {

  private IStatus status;
  private  IStatusAction action;

  public ExampleStatusEnumAvailableForActionTest(IStatus status, IStatusAction action) {
    this.status = status;
    this.action = action;
  }

  @Parameterized.Parameters
  public static List<Object[]> isEmptyData() {
    return Arrays.asList(new Object[][]{
        {STATUS_1, ACTION_1},
        {STATUS_1, ACTION_2},
        {STATUS_1, ACTION_6},
        {STATUS_1, ACTION_7},
        {STATUS_1, ACTION_5},
        {STATUS_1, ACTION_3},
        {STATUS_3, ACTION_8},
        {STATUS_3, ACTION_5},
        {STATUS_3, ACTION_4},
        {STATUS_5, ACTION_6},
        {STATUS_5, ACTION_7},
        {STATUS_5, ACTION_1},
    });
  }

  @Test
  public void availableForAction() throws Exception {
    Assert.assertTrue(status.availableForAction(action));
  }


}