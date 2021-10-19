package com.zelu.authorizecode.utils.Rquest.entity;

import lombok.Data;

/**
 * @author wangqiang
 * @Date: 2021/9/16 14:10
 * 参数实体对象的参数3(options参数)
 */
@Data
public class RequestOption {
    private String bmac;
    private Integer timeout;
    private String ssid;//wife的名字
    private String[] dict;//wife的密码
}
