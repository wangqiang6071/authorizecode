package com.zelu.authorizecode.service.impl;

import com.mongodb.client.result.DeleteResult;
import com.zelu.authorizecode.common.ServerResponse;
import com.zelu.authorizecode.entity.ScanerGroup;
import com.zelu.authorizecode.entity.ScanerPlugsGroup;
import com.zelu.authorizecode.service.IScanerGroupService;
import com.zelu.authorizecode.utils.JWTUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 插件组 服务实现类
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-16
 */
@Service
public class ScanerGroupServiceImpl implements IScanerGroupService {
    @Autowired
    private MongoTemplate repository;

    @Override
    public ServerResponse<String> add_update_plug_group(ScanerGroup group) {
        //新增
        if(StringUtils.isBlank(group.getGroupNo())){
            Criteria criteria = new Criteria();
            criteria.andOperator(criteria.where("group_name").is(group.getGroupName()));
            Query query = new Query(criteria);
            ScanerGroup plugrop = repository.findOne(query, ScanerGroup.class);
            if(plugrop!=null){
                return ServerResponse.createByError("插件组的名字已存在");
            }
            group.setGroupNo(JWTUtils.getStringId());
            group.setCreateTime(LocalDateTime.now());
            final ScanerGroup groups = repository.save(group);
            if(groups==null){
                return ServerResponse.createByError("插件组新增操作失败");
            }
        }else{
            final ScanerGroup byId = repository.findById(group.getGroupNo(), ScanerGroup.class);
            if(byId==null){
                return ServerResponse.createByError("插件组的编号不存在");
            }
            if(!StringUtils.equals(byId.getGroupName(),group.getGroupName())){
                Criteria criteria = new Criteria();
                criteria.andOperator(criteria.where("group_name").is(group.getGroupName()));
                Query query = new Query(criteria);
                ScanerGroup plugrop = repository.findOne(query, ScanerGroup.class);
                if(plugrop!=null){
                    return ServerResponse.createByError("更新插件组的名字已存在");
                }
                group.setCreateTime(LocalDateTime.now());
                final ScanerGroup groups = repository.save(group);
                if(groups==null){
                    return ServerResponse.createByError("插件组更新操作失败");
                }
            }
        }
        return ServerResponse.createBySuccess("插件组操作成功");
    }

    @Override
    public ServerResponse<String> delete_plug_group(String groupNo) {
        if(StringUtils.isBlank(groupNo)){
            return ServerResponse.createByError("编号不能为空");
        }
        //查询一下是否存在组数据
        Criteria criteria = new Criteria();
        criteria.andOperator(criteria.where("group_no").is(groupNo));
        Query query = new Query(criteria);
        final List<ScanerPlugsGroup> plugsGroup = repository.find(query, ScanerPlugsGroup.class);
        if(plugsGroup.size()>0){
            return ServerResponse.createByError("插件组使用中无法删除");
        }
        //删除插件组名字
        final DeleteResult remove = repository.remove(repository.findById(groupNo, ScanerGroup.class));
        if(remove.getDeletedCount()==0){
            return ServerResponse.createByError("插件组删除失败");
        }
        return ServerResponse.createBySuccess("插件组删除成功");
    }

    //查询所有插件组
    @Override
    public ServerResponse<List<ScanerGroup>> plugs_group_list() {
        final List<ScanerGroup> all = repository.findAll(ScanerGroup.class);
        return ServerResponse.createBySuccess(all);
    }
}
