package com.zelu.authorizecode.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 授权码与模块的绑定关系 服务器与授权共有表
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("authorty_bind_model")
public class AuthortyBindModel extends Model<AuthortyBindModel> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 当前记录的编号
     */
    private String bindNo;

    /**
     * 授权码的编号
     */
    private String authortyNo;

    /**
     * 系统模块编号
     */
    private String modelNo;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
