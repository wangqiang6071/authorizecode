package com.zelu.authorizecode.service.impl;

import com.mongodb.client.result.DeleteResult;
import com.zelu.authorizecode.common.ServerResponse;
import com.zelu.authorizecode.entity.ScanerPlugs;
import com.zelu.authorizecode.entity.ScanerPlugsGroup;
import com.zelu.authorizecode.entity.ScanerPlugsParams;
import com.zelu.authorizecode.service.IScanerPlugsService;
import com.zelu.authorizecode.utils.JWTUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 所有插件列表 服务实现类
 * </p>
 * @author wangqiang
 * @since 2021-09-16
 */
@Service
public class ScanerPlugsServiceImpl implements IScanerPlugsService {

    @Autowired
    private MongoTemplate repository;

    //插件 增加(plugsNo为空) 更新(plugsNo非空)
    @Override
    public ServerResponse<String> add_update_plugs(ScanerPlugs params) {
       //新增
        if(StringUtils.isBlank(params.getPlugsNo())){
            Criteria criteria = new Criteria();
            criteria.andOperator(criteria.where("plugs_id").is(params.getPlugsId()));
            Query query = new Query(criteria);
            ScanerPlugs plug = repository.findOne(query, ScanerPlugs.class);
            if(plug!=null){
                return ServerResponse.createByError("添加的插件id已存在");
            }
            params.setPlugsNo(JWTUtils.getStringId());
            params.setCreateTime(LocalDateTime.now());
            final ScanerPlugs save = repository.save(params);
            if(save==null){
                return ServerResponse.createByError("插件新增操作失败");
            }
        }else{
            final ScanerPlugs byId = repository.findById(params.getPlugsNo(), ScanerPlugs.class);
            if(byId==null){
                return ServerResponse.createByError("插件编号有误");
            }
            if(!StringUtils.equals(byId.getPlugsId(),params.getPlugsId())){
                Criteria criteria = new Criteria();
                criteria.andOperator(criteria.where("plugs_id").is(params.getPlugsId()));
                Query query = new Query(criteria);
                ScanerPlugs plug = repository.findOne(query, ScanerPlugs.class);
                if(plug!=null){
                    return ServerResponse.createByError("更新添加的插件id已存在");
                }
                params.setCreateTime(LocalDateTime.now());
                final ScanerPlugs save = repository.save(params);
                if(save==null){
                    return ServerResponse.createByError("插件更新操作失败");
                }
            }else{
                params.setCreateTime(LocalDateTime.now());
                final ScanerPlugs save = repository.save(params);
                if(save==null){
                    return ServerResponse.createByError("插件更新操作失败");
                }
            }
        }
        return ServerResponse.createBySuccess("插件操作成功");
    }

    //插件 删除
    @Override
    public ServerResponse<String> delete_plugs(String plugsNo) {
        //查一下插件是否被使用
        Criteria criteria = new Criteria();
        criteria.andOperator(criteria.where("plugsNo").is(plugsNo));
        Query query = new Query(criteria);
        List<ScanerPlugsGroup>  list = repository.find(query, ScanerPlugsGroup.class);
        if(list.size()>0){
            return ServerResponse.createByError("插件已被使用");
        }
        //进行删除插件
        final DeleteResult remove = repository.remove(repository.findById(plugsNo,ScanerPlugs.class));
        if(remove.getDeletedCount()==0){
            return ServerResponse.createByError("插件删除失败");
        }
        return ServerResponse.createBySuccess("插件删除成功");
    }

    //插件 列表
    @Override
    public ServerResponse<List<ScanerPlugs>> plugs_list() {
        final List<ScanerPlugs> all = repository.findAll(ScanerPlugs.class);
        return ServerResponse.createBySuccess(all);
    }

    //========================================

    //插件参数 增加(plugsNo为空) 更新(plugsNo非空)
    public ServerResponse<String> add_update_plugs_params(ScanerPlugsParams params) {
        if(StringUtils.isBlank(params.getPlugsNo())){
            return ServerResponse.createByError("插件编号不能为空");
        }
        //查询一下插件是否存在
        final ScanerPlugs plug = repository.findById(params.getPlugsNo(), ScanerPlugs.class);
        if(plug==null){
            return ServerResponse.createByError("插件id不存在");
        }
        params.setCreateTime(LocalDateTime.now());
        final ScanerPlugsParams save = repository.save(params);
        if(save==null){
            return ServerResponse.createByError("插件参数操作失败");
        }
        return ServerResponse.createBySuccess("插件参数操作成功");
    }
    //插件参数 删除
    public ServerResponse<String> delete_plugs_params(String plugsNo) {
        final DeleteResult remove = repository.remove(repository.findById(plugsNo, ScanerPlugsParams.class));
        if(remove.getDeletedCount()==0){
            return ServerResponse.createByError("插件参数删除失败");
        }
        return ServerResponse.createBySuccess("插件参数删除成功");
    }

    //插件参数 查询
    public ServerResponse<ScanerPlugsParams> get_plugs_params_by_plugno(String plugsNo) {
        final ScanerPlugsParams params = repository.findById(plugsNo, ScanerPlugsParams.class);
        if(params==null){
            return ServerResponse.createByError("当前插件还没有配置参数");
        }
        return ServerResponse.createBySuccess(params);
    }
}
