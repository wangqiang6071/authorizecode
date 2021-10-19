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
 * 授权码表 系统私有表
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("authorty_code")
public class AuthortyCode extends Model<AuthortyCode> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 授权码编号
     */
    private String authortyNo;

    /**
     * 生成的授权码=给到用户
     */
    private String authortyCode;

    /**
     * 授权码的密钥=给到用户
     */
    private String authortyKey;

    /**
     * 有效期
     */
    private LocalDateTime periodofValidity;

    /**
     * 系统的唯一标识
     */
    private String systemUuid;

    /**
     * 系统的硬盘的编号
     */
    private String systemDiskno;

    /**
     * 公司名字
     */
    private String companyName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
