package com.zelu.authorizecode.service.impl;

import com.zelu.authorizecode.entity.AuthortySystemBindModel;
import com.zelu.authorizecode.dao.AuthortySystemBindModelMapper;
import com.zelu.authorizecode.service.IAuthortySystemBindModelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class AuthortySystemBindModelServiceImpl extends ServiceImpl<AuthortySystemBindModelMapper, AuthortySystemBindModel> implements IAuthortySystemBindModelService {

}
