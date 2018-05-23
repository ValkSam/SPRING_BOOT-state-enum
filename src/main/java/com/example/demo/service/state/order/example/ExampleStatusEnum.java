package com.example.demo.service.state.order.example;

import com.example.demo.service.state.order.IStatus;
import com.example.demo.service.state.order.IStatusAction;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ValkSam
 */
public enum ExampleStatusEnum implements IStatus {
    STATUS_1(1, false) {
        @Override
        public void initSchema(Map<IStatusAction, IStatus> schemaMap) {
            schemaMap.put(ExampleActionEnum.ACTION_1, STATUS_2);
            schemaMap.put(ExampleActionEnum.ACTION_2, STATUS_3);
            schemaMap.put(ExampleActionEnum.ACTION_6, STATUS_3);
            schemaMap.put(ExampleActionEnum.ACTION_5, STATUS_6);
            schemaMap.put(ExampleActionEnum.ACTION_3, STATUS__4);
            schemaMap.put(ExampleActionEnum.ACTION_7, STATUS__4);
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
            schemaMap.put(ExampleActionEnum.ACTION_8, STATUS__5);
            schemaMap.put(ExampleActionEnum.ACTION_5, STATUS_2);
            schemaMap.put(ExampleActionEnum.ACTION_4, STATUS_8);
        }
    },
    STATUS__4(4, true) {
        @Override
        public void initSchema(Map<IStatusAction, IStatus> schemaMap) {
        }
    },
    STATUS__5(5, true) {
        @Override
        public void initSchema(Map<IStatusAction, IStatus> schemaMap) {
            schemaMap.put(ExampleActionEnum.ACTION_6, STATUS__5);
            schemaMap.put(ExampleActionEnum.ACTION_7, STATUS_3);
            schemaMap.put(ExampleActionEnum.ACTION_1, STATUS_7);
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
    private final boolean permittedForSomeExternalAction; //we can use it to check if some action is permitted for particular entity status

    private final Map<IStatusAction, IStatus> schemaMap = new HashMap<>();

    ExampleStatusEnum(Integer code, Boolean permittedForSomeExternalAction) {
        this.code = code;
        this.permittedForSomeExternalAction = permittedForSomeExternalAction;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public boolean isPermittedForSomeExternalAction() {
        return permittedForSomeExternalAction;
    }

    @Override
    public Map<IStatusAction, IStatus> getSchemaMap() {
        return schemaMap;
    }

    static {
        for (IStatus status : ExampleStatusEnum.values()) {
            status.initSchema(status.getSchemaMap());
        }
        // check schemaMap
        IStatus.getBeginState(ExampleStatusEnum.class);
    }

}
