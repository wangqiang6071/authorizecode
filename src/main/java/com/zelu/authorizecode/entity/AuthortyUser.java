package com.zelu.authorizecode.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * website后台用户
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("authorty_user")
public class AuthortyUser extends Model<AuthortyUser> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
