package com.zelu.authorizecode.service.impl;

import com.zelu.authorizecode.entity.AuthortyCode;
import com.zelu.authorizecode.dao.AuthortyCodeMapper;
import com.zelu.authorizecode.service.IAuthortyCodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 授权码表 系统私有表 服务实现类
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-03
 */
@Service
public class AuthortyCodeServiceImpl extends ServiceImpl<AuthortyCodeMapper, AuthortyCode> implements IAuthortyCodeService {

}
