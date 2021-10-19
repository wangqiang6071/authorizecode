package com.zelu.authorizecode.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zelu.authorizecode.dao.AuthortyModelMapper;
import com.zelu.authorizecode.entity.AuthortyModel;
import com.zelu.authorizecode.service.IAuthortyModelService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 模块表 服务器和系统共有表 服务实现类
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-03
 */
@Service
public class AuthortyModelServiceImpl extends ServiceImpl<AuthortyModelMapper, AuthortyModel> implements IAuthortyModelService {

}
