package com.zelu.authorizecode.dao;

import com.zelu.authorizecode.entity.AuthortyCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 授权码表 系统私有表 Mapper 接口
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-03
 */
@Mapper
public interface AuthortyCodeMapper extends BaseMapper<AuthortyCode> {

}
