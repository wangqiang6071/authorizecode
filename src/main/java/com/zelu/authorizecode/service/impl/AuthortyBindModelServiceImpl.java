package com.zelu.authorizecode.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zelu.authorizecode.dao.AuthortyBindModelMapper;
import com.zelu.authorizecode.entity.AuthortyBindModel;
import com.zelu.authorizecode.service.IAuthortyBindModelService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 授权码与模块的绑定关系 服务器与授权共有表 服务实现类
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-03
 */
@Service
public class AuthortyBindModelServiceImpl extends ServiceImpl<AuthortyBindModelMapper, AuthortyBindModel> implements IAuthortyBindModelService {

}
