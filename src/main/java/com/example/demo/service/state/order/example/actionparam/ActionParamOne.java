package com.example.demo.service.state.order.example.actionparam;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Created by ValkSam
 */
@Builder
@Getter
@ToString
public class ActionParamOne implements ActionParam {
    private String someParam = null;
    private List<String> somePermissionList;
}
