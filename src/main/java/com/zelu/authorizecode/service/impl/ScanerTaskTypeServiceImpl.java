package com.zelu.authorizecode.service.impl;

import com.mongodb.client.result.DeleteResult;
import com.zelu.authorizecode.common.ServerResponse;
import com.zelu.authorizecode.entity.ScanerTaskType;
import com.zelu.authorizecode.service.IScanerTaskTypeService;
import com.zelu.authorizecode.utils.JWTUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 任务类型 服务实现类
 * </p>
 * @author wangqiang
 * @since 2021-09-16
 */
@Service
public class ScanerTaskTypeServiceImpl implements IScanerTaskTypeService {
    @Autowired
    private MongoTemplate repository;
    @Override
    public ServerResponse<String> Add_UpdateTaskType(ScanerTaskType type) {
        if(StringUtils.isBlank(type.getTypeName())){
            return ServerResponse.createByError("类型名字不能为空");
        }
        if(StringUtils.isBlank(type.getTypeNo())){
            //检查一下名字是否存在
            Criteria criteria = new Criteria();
            criteria.andOperator(criteria.where("type_name").is(type.getTypeName()));
            Query query = new Query(criteria);
            final ScanerTaskType scanerTaskTypes = repository.findOne(query, ScanerTaskType.class);
            if(scanerTaskTypes!=null){
                return ServerResponse.createByError("类型的名字已存在");
            }
            type.setTypeNo(JWTUtils.getStringId());
            final ScanerTaskType save = repository.save(type);
            if(save==null){
                return ServerResponse.createByError("任务类别新增操作失败");
            }
        }else{
            final ScanerTaskType Type = repository.findById(type.getTypeNo(), ScanerTaskType.class);
            if(Type==null){
                return ServerResponse.createByError("更新类型typeNo错误");
            }
            if(!StringUtils.equals(Type.getTypeName(),type.getTypeName())){
                //检查一下名字是否存在
                Criteria criteria = new Criteria();
                criteria.andOperator(criteria.where("type_name").is(type.getTypeName()));
                Query query = new Query(criteria);
                final ScanerTaskType scanerTaskTypes = repository.findOne(query, ScanerTaskType.class);
                if(scanerTaskTypes!=null){
                    return ServerResponse.createByError("更新类型的名字已存在");
                }
                final ScanerTaskType save = repository.save(type);
                if(save==null){
                    return ServerResponse.createByError("任务类别更新操作失败");
                }
            }
        }
        return ServerResponse.createBySuccess("任务类别操作成功");
    }

    @Override
    public ServerResponse<String> DeleteTaskType(String typeNo) {
        if(StringUtils.isBlank(typeNo)){
            return ServerResponse.createByError("类型编号不能为空");
        }
        Criteria criteria = new Criteria();
        criteria.andOperator(criteria.where("typeNo").is(typeNo));
        Query query = new Query(criteria);
        final DeleteResult remove = repository.remove(query, ScanerTaskType.class);
        if(remove.getDeletedCount()==0){
            return ServerResponse.createByError("类型删除失败");
        }
        return ServerResponse.createBySuccess("类型删除成功");
    }

    @Override
    public ServerResponse<List<ScanerTaskType>> TaskTypeList() {
        final List<ScanerTaskType> all = repository.findAll(ScanerTaskType.class);
        return ServerResponse.createBySuccess(all);
    }
}
