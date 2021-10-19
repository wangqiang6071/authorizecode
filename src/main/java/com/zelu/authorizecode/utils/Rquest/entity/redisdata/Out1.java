package com.zelu.authorizecode.utils.Rquest.entity.redisdata;

import lombok.Data;

import java.util.List;

/**
 * @author wangqiang
 * @Date: 2021/9/18 14:21
 */
@Data
public class Out1 {
    private Out2 out;
    private String status;
    private String error;
    private Long time;
}
