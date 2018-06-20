package com.example.demo.service.state.order.example.actionparam;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigInteger;

/**
 * Created by ValkSam
 */
@Builder
@Getter
@ToString
public class ActionParamTwo implements ActionParam {
    private BigInteger someParamBigInteger = null;
    private int someParamInteger;
    private String someParamString = null;
}
