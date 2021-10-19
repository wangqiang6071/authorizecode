package com.zelu.authorizecode.dao;

import com.zelu.authorizecode.entity.AuthortyUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * website后台用户 Mapper 接口
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-03
 */
@Mapper
public interface AuthortyUserMapper extends BaseMapper<AuthortyUser> {

}
