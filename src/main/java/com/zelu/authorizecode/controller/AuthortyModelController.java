package com.zelu.authorizecode.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zelu.authorizecode.common.ServerResponse;
import com.zelu.authorizecode.dao.AuthortyModelMapper;
import com.zelu.authorizecode.entity.AuthortyBindModel;
import com.zelu.authorizecode.entity.AuthortyModel;
import com.zelu.authorizecode.service.IAuthortyBindModelService;
import com.zelu.authorizecode.service.IAuthortyModelService;
import com.zelu.authorizecode.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 * 模块表 服务器和系统共有表 前端控制器
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-03
 */
@Slf4j
@RestController
@RequestMapping("/model")
public class AuthortyModelController {
    @Autowired
    private IAuthortyModelService modelService;
    @Autowired
    private AuthortyModelMapper modelMapper;
    @Autowired
    private IAuthortyBindModelService bindModelService;
    //模块的增
    @ResponseBody
    @PostMapping("add_model")
    public ServerResponse<String> addModel(String modelName){
        if(StringUtils.isBlank(modelName)){
            return ServerResponse.createByError("模块名字不能为空");
        }
        QueryWrapper<AuthortyModel>modelWrapper=new QueryWrapper<>();
        modelWrapper.eq("model_name",modelName);
        final AuthortyModel one = modelService.getOne(modelWrapper);
        if(one!=null){
            return ServerResponse.createByError("模块名字已存在");
        }
        AuthortyModel model=new AuthortyModel();
        model.setModelNo(JWTUtils.getStringId());
        model.setModelName(modelName);
        final boolean save = modelService.save(model);
        if(!save){
           return ServerResponse.createByError("模块添加失败");
        }
        return ServerResponse.createBySuccess("模块添加成功");
    }
    //模块的删除
    @ResponseBody
    @GetMapping("delete_model")
    public ServerResponse<String> deleteModel(String modelNo){
        if(StringUtils.isBlank(modelNo)){
            return ServerResponse.createByError("模块的编号不能为空");
        }
        QueryWrapper<AuthortyModel>modelWrapper=new QueryWrapper<>();
        modelWrapper.eq("model_no",modelNo);
        final AuthortyModel one = modelService.getOne(modelWrapper);
        if(one==null){
            return ServerResponse.createByError("模块编号不存在");
        }
        QueryWrapper<AuthortyBindModel>bindmodelWrapper=new QueryWrapper<>();
        bindmodelWrapper.eq("model_no",modelNo);
        final List<AuthortyBindModel> list = bindModelService.list(bindmodelWrapper);
        if(list.size()>0){
            return ServerResponse.createByError("删除的模块已经别占用");
        }
        final boolean remove = modelService.remove(modelWrapper);
        if(!remove){
            return ServerResponse.createByError("删除失败");
        }
        return ServerResponse.createBySuccess("删除成功");
    }
    //模块的改
    @ResponseBody
    @PostMapping("update_model")
    public ServerResponse<String> updateModel(String modelNo,String modelName){
        if(StringUtils.isBlank(modelNo)){
            return ServerResponse.createByError("模块的编号不能为空");
        }
        QueryWrapper<AuthortyModel>modelWrapper=new QueryWrapper<>();
        modelWrapper.eq("model_no",modelNo);
        final AuthortyModel one = modelService.getOne(modelWrapper);
        if(one==null){
            return ServerResponse.createByError("模块编号不存在");
        }
        if(StringUtils.isBlank(modelName)){
            return ServerResponse.createByError("模块名字不能为空");
        }
        if(!StringUtils.equals(one.getModelName(),modelName)){
            //检查是否有重复的数据
            QueryWrapper<AuthortyModel>modelWrappers=new QueryWrapper<>();
            modelWrappers.eq("model_name",modelName);
            final Integer integer = modelMapper.selectCount(modelWrappers);
            log.info("integer==>"+integer);
            if(integer>0){
                return ServerResponse.createByError("模块的名字已重复");
            }
        }
        one.setModelName(modelName);
        final boolean b = modelService.updateById(one);
        if(!b){
            return ServerResponse.createByError("模块修改失败");
        }
        return ServerResponse.createBySuccess("模块修改成功");
    }
    //模块的查
    @ResponseBody
    @PostMapping("select_model")
    public ServerResponse<IPage<AuthortyModel>> selectModel(@RequestBody AuthortyModel model){
        Page<AuthortyModel> page =  new Page<>(model.getPage_index(),model.getPage_size());
        QueryWrapper<AuthortyModel> queryWrapper=new QueryWrapper<>();
        if(StringUtils.isNotBlank(model.getModelName())){
            queryWrapper.or().like("model_name",model.getModelName());
        }
        IPage<AuthortyModel> mapIPages = modelService.page(page, queryWrapper);
        return ServerResponse.createBySuccess(mapIPages);
    }
}
