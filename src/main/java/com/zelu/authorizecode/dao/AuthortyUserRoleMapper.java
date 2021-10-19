package com.zelu.authorizecode.dao;

import com.zelu.authorizecode.entity.AuthortyUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户角色关系  Mapper 接口
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-03
 */
@Mapper
public interface AuthortyUserRoleMapper extends BaseMapper<AuthortyUserRole> {

}
