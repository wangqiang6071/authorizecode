package com.zelu.authorizecode.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zelu.authorizecode.common.ServerResponse;
import com.zelu.authorizecode.entity.AuthortyUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zelu.authorizecode.entity.params.AuthortyParams;
import java.util.Map;

/**
 * <p>
 * website后台用户 服务类
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-03
 */
public interface IAuthortyUserService extends IService<AuthortyUser> {

    ServerResponse<Map<String, Object>> userAccountLogin(String account, String password);

    ServerResponse<String> userAccountLoginOut();

    ServerResponse<IPage<AuthortyUser>> User_List(AuthortyParams param);

    ServerResponse<String> updateUser(AuthortyParams account);

    ServerResponse<String> deleteUser(String user_no);

    ServerResponse<String> addUser(AuthortyParams account);

}
