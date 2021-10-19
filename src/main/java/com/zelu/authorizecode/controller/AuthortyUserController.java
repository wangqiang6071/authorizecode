package com.zelu.authorizecode.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zelu.authorizecode.common.ServerResponse;
import com.zelu.authorizecode.entity.AuthortyModel;
import com.zelu.authorizecode.entity.AuthortyRole;
import com.zelu.authorizecode.entity.AuthortyUser;
import com.zelu.authorizecode.entity.params.AuthortyParams;
import com.zelu.authorizecode.service.IAuthortyUserRoleService;
import com.zelu.authorizecode.service.IAuthortyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * <p>
 * website后台用户 前端控制器
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-03
 */
@RestController
@RequestMapping("/user")
public class AuthortyUserController {
    @Autowired
    private IAuthortyUserService UserService;
    @Autowired
    private IAuthortyUserRoleService userRoleService;


    //账号登陆
    @PostMapping(value = "/login")
    public ServerResponse<Map<String, Object>> UserAccountLogin(@RequestParam("account") String account, @RequestParam("password") String password){
        return UserService.userAccountLogin(account,password);
    }

    //添加账号
    @PostMapping(value = "/register",produces = "application/json")
    public ServerResponse<String> addUser(@RequestBody AuthortyParams account){
        return UserService.addUser(account);
    }

    //删除用户
    @GetMapping(value = "/delete_user")
    public ServerResponse<String> DeleteUser(String user_no){
        return UserService.deleteUser(user_no);
    }

    //编辑用户
    @PostMapping(value = "/update_user",produces = "application/json")
    public ServerResponse<String> UpdateUser(@RequestBody AuthortyParams account){
        return UserService.updateUser(account);
    }
    //查用户
    @PostMapping("list")
    public ServerResponse<IPage<AuthortyUser>> UserList(@RequestBody AuthortyParams param){
        return UserService.User_List(param);
    }

    //TODO 编写接口文档测试
    //根据用户查询对应的模块
    @PostMapping(value = "model_by_user")
    public ServerResponse<List<AuthortyModel>> ModelByUser(String userNo){
        return userRoleService.SelectByUserModel(userNo);
    }
    //当前用户是哪个角色
    @PostMapping(value = "user_role")
    public ServerResponse<AuthortyRole> UserRole(String userNo){
        return userRoleService.SelectByUserRole(userNo);
    }

    //用户与角色绑定
    @PostMapping(value = "user_bind_role")
    public ServerResponse<String> UserBindRole(String userNo,String roleNo){
        return userRoleService.AddUserRole(userNo,roleNo);
    }

    //账号退出
    @GetMapping(value = "loginout")
    public ServerResponse<String> UserAccountLoginOut(){
        return UserService.userAccountLoginOut();
    }

    //提示用户需要的登陆的接口
    @GetMapping(value = "/needlogin")
    public ServerResponse<String> needlogin(){
        return ServerResponse.createByError("请登陆账号");
    }
}
