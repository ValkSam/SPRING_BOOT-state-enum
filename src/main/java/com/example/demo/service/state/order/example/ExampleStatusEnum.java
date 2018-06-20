package com.example.demo.service.state.order.example;

import com.example.demo.service.state.order.IStatus;
import com.example.demo.service.state.order.IStatusAction;
import com.example.demo.service.state.order.example.actionparam.ActionParam;

import java.util.HashMap;
import java.util.Map;

import static com.example.demo.service.state.order.example.ExampleActionEnum.ACTION_1;
import static com.example.demo.service.state.order.example.ExampleActionEnum.ACTION_2;
import static com.example.demo.service.state.order.example.ExampleActionEnum.ACTION_3;
import static com.example.demo.service.state.order.example.ExampleActionEnum.ACTION_4;
import static com.example.demo.service.state.order.example.ExampleActionEnum.ACTION_5;
import static com.example.demo.service.state.order.example.ExampleActionEnum.ACTION_6;
import static com.example.demo.service.state.order.example.ExampleActionEnum.ACTION_7;
import static com.example.demo.service.state.order.example.ExampleActionEnum.ACTION_8;

/**
 * Created by ValkSam
 */
public enum ExampleStatusEnum implements IStatus<ActionParam> {
    STATUS_1(1, false) {
        @Override
        public void initSchema(Map<IStatusAction, IStatus> schemaMap) {
            schemaMap.put(ACTION_1, STATUS_2);
            schemaMap.put(ACTION_2, STATUS_3);
            schemaMap.put(ACTION_6, STATUS_3);
            schemaMap.put(ACTION_5, STATUS_6);
            schemaMap.put(ACTION_3, STATUS_4);
            schemaMap.put(ACTION_7, STATUS_4);
        }
    },
    STATUS_2(2, false) {
        @Override
        public void initSchema(Map<IStatusAction, IStatus> schemaMap) {
        }
    },
    STATUS_3(3, true) {
        @Override
        public void initSchema(Map<IStatusAction, IStatus> schemaMap) {
            schemaMap.put(ACTION_8, STATUS_5);
            schemaMap.put(ACTION_5, STATUS_2);
            schemaMap.put(ACTION_4, STATUS_8);
        }
    },
    STATUS_4(4, true) {
        @Override
        public void initSchema(Map<IStatusAction, IStatus> schemaMap) {
        }
    },
    STATUS_5(5, true) {
        @Override
        public void initSchema(Map<IStatusAction, IStatus> schemaMap) {
            schemaMap.put(ACTION_6, STATUS_5);
            schemaMap.put(ACTION_7, STATUS_3);
            schemaMap.put(ACTION_1, STATUS_7);
        }
    },
    STATUS_6(6, true) {
        public void initSchema(Map<IStatusAction, IStatus> schemaMap) {
        }
    },
    STATUS_7(7, true) {
        public void initSchema(Map<IStatusAction, IStatus> schemaMap) {
        }
    },
    STATUS_8(8, true) {
        public void initSchema(Map<IStatusAction, IStatus> schemaMap) {
        }
    };

    private final Integer code;
    private final boolean permittedForAccess; //we can use it to check if some action is permitted for particular entity status

    private final Map<IStatusAction, IStatus> schemaMap = new HashMap<>();

    ExampleStatusEnum(Integer code, Boolean permittedForAccess) {
        this.code = code;
        this.permittedForAccess = permittedForAccess;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public boolean isPermittedForAccess() {
        return permittedForAccess;
    }

    @Override
    public Map<IStatusAction, IStatus> getSchemaMap() {
        return schemaMap;
    }

    static {
        for (IStatus<ActionParam> status : ExampleStatusEnum.values()) {
            status.initSchema(status.getSchemaMap());
        }
        // check schemaMap
        IStatus.getBeginState(ExampleStatusEnum.class);
    }

}
