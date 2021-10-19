package com.zelu.authorizecode.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 模块表 服务器和系统共有表
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-03
 */
@Data
@TableName("authorty_model")
public class AuthortyModel extends Model<AuthortyModel> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 模块的编号
     */
    private String modelNo;

    /**
     * 模块的名字
     */
    private String modelName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    @TableField(exist = false)
    private Integer page_index=1;

    @TableField(exist = false)
    private Integer page_size=10;
}
