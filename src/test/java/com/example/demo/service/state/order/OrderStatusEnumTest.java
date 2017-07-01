package com.example.demo.service.state.order;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ValkSam on 01.07.2017.
 */
public class OrderStatusEnumTest {
  @Test
  public void nextState() throws Exception {

  }

  @Test
  public void availableForAction() throws Exception {

  }

  @Test
  public void getAvailableForActionStatusesList() throws Exception {

  }

  @Test
  public void getAvailableForActionStatusesList1() throws Exception {

  }

  @Test
  public void convert() throws Exception {

  }

  @Test
  public void convert1() throws Exception {

  }

  @Test
  public void getBeginState() throws Exception {
    OrderStatusEnum state = OrderStatusEnum.CREATED;
    Assert.assertEquals(state, OrderStatusEnum.getBeginState());
  }

  @Test
  public void getEndStatesSet() throws Exception {

  }

  @Test
  public void getMiddleStatesSet() throws Exception {

  }

  @Test
  public void getCode() throws Exception {

  }

}