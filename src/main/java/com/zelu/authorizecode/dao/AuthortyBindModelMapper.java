package com.zelu.authorizecode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zelu.authorizecode.entity.AuthortyBindModel;
import com.zelu.authorizecode.entity.AuthortySystemBindModel;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 * @author wangqiang
 * @since 2021-09-03
 *
 */
@Mapper
public interface AuthortyBindModelMapper extends BaseMapper<AuthortyBindModel> {

}
