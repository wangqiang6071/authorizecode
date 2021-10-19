package com.zelu.authorizecode.entity.params;

import lombok.Data;

/**
 * @author wangqiang
 * @Date: 2021/9/3 14:23
 */
@Data
public class AuthortyParams {
    /**
     * 用户编号
     */
    private String userNo;

    /**
     * 用户账号
     */
    private String account;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户名字
     */
    private String userName;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 账号状态:1账号正常 2账号禁止登陆
     */
    private Integer status;

    /**
     * 用户性别:1男 2女
     */
    private Integer sex;
    /**当前页面**/
    private Integer index=1;
    /**分页条数**/
    private Integer size=10;
}
