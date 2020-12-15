package com.walker.core.protocol;

import lombok.Builder;
import lombok.Data;

/**
 * @author dell
 * @date 2020/12/15 16:59
 **/
@Data
@Builder
public class RpcProtoReq {
    private String className;
    private String methodName;
    private Object[] params;
}
