package com.zelu.authorizecode.emus;

/**
 * @author wangqiang
 * @Date: 2021/6/30 16:38
 */
public enum ShiroLoginType {
    PASSWORD("password"), // 密码登录
    NOPASSWD("nopassword"); // 免密登录

    private String code;// 状态值

    private ShiroLoginType(String code) {
        this.code = code;
    }
    public String getCode () {
        return code;
    }
}
