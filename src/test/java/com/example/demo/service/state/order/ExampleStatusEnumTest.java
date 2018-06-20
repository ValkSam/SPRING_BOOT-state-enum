package com.example.demo.service.state.order;

import com.example.demo.service.state.exception.ActionConditionFailedException;
import com.example.demo.service.state.exception.ActionParamsNeededException;
import com.example.demo.service.state.order.example.ExampleStatusEnum;
import com.example.demo.service.state.order.example.actionparam.ActionParam;
import com.example.demo.service.state.order.example.actionparam.ActionParamOne;
import com.example.demo.service.state.order.example.actionparam.ActionParamTwo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static com.example.demo.service.state.order.example.ExampleActionEnum.ACTION_1;
import static com.example.demo.service.state.order.example.ExampleActionEnum.ACTION_2;
import static com.example.demo.service.state.order.example.ExampleActionEnum.ACTION_3;
import static com.example.demo.service.state.order.example.ExampleActionEnum.ACTION_5;
import static com.example.demo.service.state.order.example.ExampleActionEnum.ACTION_6;
import static com.example.demo.service.state.order.example.ExampleActionEnum.ACTION_7;
import static com.example.demo.service.state.order.example.ExampleStatusEnum.STATUS_1;
import static com.example.demo.service.state.order.example.ExampleStatusEnum.STATUS_2;
import static com.example.demo.service.state.order.example.ExampleStatusEnum.STATUS_3;
import static com.example.demo.service.state.order.example.ExampleStatusEnum.STATUS_4;
import static com.example.demo.service.state.order.example.ExampleStatusEnum.STATUS_5;
import static com.example.demo.service.state.order.example.ExampleStatusEnum.STATUS_6;
import static com.example.demo.service.state.order.example.ExampleStatusEnum.STATUS_7;
import static com.example.demo.service.state.order.example.ExampleStatusEnum.STATUS_8;
import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TEN;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by ValkSam
 */
@RunWith(Theories.class)
public class ExampleStatusEnumTest {

    @Test
    public void collectAllSchemaMapNodesSet() throws Exception {
        Set<IStatus> fullList = new TreeSet<>(IStatus.collectAllSchemaMapNodesSet(ExampleStatusEnum.class));
        Set<IStatus> collectedFullList = new TreeSet<>();
        collectedFullList.addAll(IStatus.getMiddleStatesSet(ExampleStatusEnum.class));
        collectedFullList.addAll(IStatus.getEndStatesSet(ExampleStatusEnum.class));
        System.out.println(collectedFullList);
        Assert.assertArrayEquals(fullList.toArray(), collectedFullList.toArray());
    }

    @DataPoints
    public static Object[][] nextStateData = new Object[][]{
            {STATUS_1, ACTION_2, STATUS_3, ActionParamOne.builder()
                    .someParam("VALID_PARAM_VALUE")
                    .somePermissionList(Collections.singletonList("SOME_VALID_PERMISSION"))
                    .build()},
            {STATUS_1, ACTION_1, STATUS_2},
            {STATUS_1, ACTION_6, STATUS_3},
            {STATUS_1, ACTION_7, STATUS_4},
            {STATUS_1, ACTION_5, STATUS_6, ActionParamTwo.builder()
                    .someParamString("VALID_PARAM_VALUE")
                    .someParamBigInteger(TEN)
                    .someParamInteger(20)
                    .build()},
            {STATUS_1, ACTION_3, STATUS_4},
    };

    @Theory
    public void nextState(Object[] data) throws Exception {
        IStatus state = (IStatus) data[0];
        IStatusAction action = (IStatusAction) data[1];
        IStatus nextState = (IStatus) data[2];
        ActionParam param = data.length < 4 ? null : (ActionParam) data[3];
        if (param == null) {
            Assert.assertEquals(nextState, state.nextState(action));
        } else {
            Assert.assertEquals(nextState, state.nextState(action, data[3]));
        }
    }

    @Test(expected = ActionParamsNeededException.class)
    public void nextStateFailByAbsentParam() throws Exception {
        ExampleStatusEnum state = STATUS_1;
        state.nextState(ACTION_2);
    }

    @Test(expected = ActionConditionFailedException.class)
    public void nextStateFailByParam() throws Exception {
        ExampleStatusEnum state = STATUS_1;
        ExampleStatusEnum nextState = STATUS_3;
        ActionParam param = ActionParamOne.builder()
                .someParam("INVALID_PARAM_VALUE")
                .somePermissionList(singletonList("SOME_VALID_PERMISSION"))
                .build();
        Assert.assertEquals(nextState, state.nextState(ACTION_2, param));
    }

    @Test(expected = ActionConditionFailedException.class)
    public void nextStateFailByPermission() throws Exception {
        ExampleStatusEnum state = STATUS_1;
        ExampleStatusEnum nextState = STATUS_3;
        ActionParam param = ActionParamOne.builder()
                .someParam("VALID_PARAM_VALUE")
                .somePermissionList(singletonList("SOME_INVALID_PERMISSION"))
                .build();
        Assert.assertEquals(nextState, state.nextState(ACTION_2, param));
    }

    @Test
    public void nextStateFailByParams() throws Exception {
        ExampleStatusEnum state = STATUS_3;
        ActionParam param = ActionParamTwo.builder()
                .someParamString("INVALID_PARAM_VALUE")
                .someParamBigInteger(ONE)
                .someParamInteger(10)
                .build();
        try {
            state.nextState(ACTION_5, param);
            fail();
        } catch (Exception e) {
            System.out.println(e);
            assertTrue(e.getClass().equals(ActionConditionFailedException.class));
        }
    }

    @Test
    public void getAvailableForAction() throws Exception {
        Set<IStatusAction> actionList = new HashSet<IStatusAction>() {{
            add(ACTION_5);
            add(ACTION_1);
        }};
        Set<IStatus> statusList = new HashSet<IStatus>() {{
            add(STATUS_1);
            add(STATUS_3);
            add(STATUS_5);
        }};
        Assert.assertArrayEquals(statusList.toArray(), IStatus.getAvailableForAction(ExampleStatusEnum.class, actionList).toArray());
    }

    @Test
    public void getBeginState() throws Exception {
        ExampleStatusEnum state = STATUS_1;
        Assert.assertEquals(state, IStatus.getBeginState(ExampleStatusEnum.class));
    }

    @Test
    public void getEndStatesSet() throws Exception {
        Set<IStatus> statusList = new HashSet<IStatus>() {{
            add(STATUS_2);
            add(STATUS_7);
            add(STATUS_4);
            add(STATUS_8);
            add(STATUS_6);
        }};
        Assert.assertArrayEquals(new TreeSet<>(statusList).toArray(), new TreeSet<>(IStatus.getEndStatesSet(ExampleStatusEnum.class)).toArray());
    }

    @Test
    public void getMiddleStatesSet() throws Exception {
        System.out.println(IStatus.getMiddleStatesSet(ExampleStatusEnum.class));
        Set<IStatus> statusList = new HashSet<IStatus>() {{
            add(STATUS_5);
            add(STATUS_3);
        }};
        Assert.assertArrayEquals(new TreeSet<>(statusList).toArray(), new TreeSet<>(IStatus.getMiddleStatesSet(ExampleStatusEnum.class)).toArray());
    }

    @Test
    public void convertTestOne() throws Exception {
        String statusName = "STATUS_1";
        Assert.assertEquals(
                IStatus.convert(ExampleStatusEnum.class, statusName),
                ExampleStatusEnum.valueOf(statusName));
    }

    @Test
    public void convertTestTwo() throws Exception {
        ExampleStatusEnum status = STATUS_2;
        int statusId = status.getCode();
        Assert.assertEquals(
                IStatus.convert(ExampleStatusEnum.class, statusId),
                status);
    }

    @Test
    public void checkStatusForbiddenForAccess() throws Exception {
        assertFalse(STATUS_2.isPermittedForAccess());
        assertTrue(STATUS_3.isPermittedForAccess());
    }

}