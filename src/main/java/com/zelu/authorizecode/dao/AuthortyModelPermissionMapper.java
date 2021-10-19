package com.zelu.authorizecode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zelu.authorizecode.entity.AuthortyModel;
import com.zelu.authorizecode.entity.AuthortyModelPermisstion;
import org.apache.ibatis.annotations.Mapper;


/**
 *
 * @author wangqiang
 * @since 2021-09-03
 */
@Mapper
public interface AuthortyModelPermissionMapper extends BaseMapper<AuthortyModelPermisstion> {

}
