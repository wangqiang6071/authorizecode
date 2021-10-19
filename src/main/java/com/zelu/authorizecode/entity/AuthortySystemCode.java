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
 * 服务器私有表
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("authorty_system_code")
public class AuthortySystemCode extends Model<AuthortySystemCode> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 系统授权编号
     */
    private String authortyNo;

    /**
     * 授权密钥
     */
    private String authortyKey;

    /**
     * 授权码
     */
    private String authortyCode;

    /**
     * 授权时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
