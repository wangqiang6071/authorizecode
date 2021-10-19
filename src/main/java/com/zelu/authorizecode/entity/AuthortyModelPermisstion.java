package com.zelu.authorizecode.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author wangqiang
 * @Date: 2021/10/14 15:33
 */
@Data
@TableName("authorty_model_permisstion")
public class AuthortyModelPermisstion extends Model<AuthortyModelPermisstion> {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String permissionName;

    private Integer modelId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
