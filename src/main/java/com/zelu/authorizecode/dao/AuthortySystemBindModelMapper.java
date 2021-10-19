package com.zelu.authorizecode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zelu.authorizecode.entity.AuthortySystemBindModel;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 * <p>
 * 授权码与模块的绑定关系 服务器与授权共有表 Mapper 接口
 * </p>
 * @author wangqiang
 * @since 2021-09-03
 *
 */
@Mapper
public interface AuthortySystemBindModelMapper extends BaseMapper<AuthortySystemBindModel> {

}
