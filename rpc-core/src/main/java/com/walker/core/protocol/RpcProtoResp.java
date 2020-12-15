package com.walker.core.protocol;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dell
 * @date 2020/12/15 16:59
 **/
@Data
@Builder
@NoArgsConstructor
public class RpcProtoResp {
    private String result;
    private boolean status;
    private Exception exception;
}
