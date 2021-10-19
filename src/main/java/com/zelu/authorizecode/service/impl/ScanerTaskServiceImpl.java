package com.zelu.authorizecode.service.impl;

import com.mongodb.client.result.DeleteResult;
import com.zelu.authorizecode.common.ServerResponse;
import com.zelu.authorizecode.entity.*;
import com.zelu.authorizecode.service.IScanerTaskService;
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
 * 新建任务 服务实现类
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-16
 */
@Service
public class ScanerTaskServiceImpl implements IScanerTaskService {
    @Autowired
    private MongoTemplate repository;

    //任务的新增和更新
    @Override
    public ServerResponse<String> Add_Update_Task(ScanerTask params) {
        //新增
        if(StringUtils.isBlank(params.getTaskNo())){
            Criteria criteria = new Criteria();
            criteria.andOperator(criteria.where("task_name").is(params.getTaskName()));
            Query query = new Query(criteria);
            final ScanerTask scanerTasks = repository.findOne(query, ScanerTask.class);
            if(scanerTasks!=null){
                return ServerResponse.createByError("任务名字已经存在");
            }
            params.setTaskNo(JWTUtils.getStringId());
            params.setCreateTime(LocalDateTime.now());
            final ScanerTask save = repository.save(params);
            if(save==null){
                return ServerResponse.createByError("任务新增操作失败");
            }
        }else{
            final ScanerTask Task = repository.findById(params.getTaskNo(), ScanerTask.class);
            if(Task==null){
                return ServerResponse.createByError("任务类型编号不存在");
            }
            if(!StringUtils.equals(Task.getTaskName(),params.getTaskName())){
                Criteria criteria = new Criteria();
                criteria.andOperator(criteria.where("task_name").is(params.getTaskName()));
                Query query = new Query(criteria);
                final ScanerTask scanerTasks = repository.findOne(query, ScanerTask.class);
                if(scanerTasks!=null){
                    return ServerResponse.createByError("更新任务名字已经存在");
                }
                params.setCreateTime(LocalDateTime.now());
                final ScanerTask save = repository.save(params);
                if(save==null){
                    return ServerResponse.createByError("任务更新操作失败");
                }
            }else{
                params.setCreateTime(LocalDateTime.now());
                final ScanerTask save = repository.save(params);
                if(save==null){
                    return ServerResponse.createByError("任务更新操作失败");
                }
            }
        }
        return ServerResponse.createBySuccess("任务操作成功");
    }

    @Override
    public ServerResponse<String> DeleteTask(String taskNo) {
        //查询一下当前任务下是否绑定了插件组或者插件
        Criteria criteria = new Criteria();
        criteria.andOperator(criteria.where("taskNo").is(taskNo));
        Query query = new Query(criteria);
        final ScanerTaskGroup scanerTaskGroup = repository.findOne(query, ScanerTaskGroup.class);
        //插件组
        if(scanerTaskGroup!=null){
            return ServerResponse.createByError("插件组编号不存在");
        }
        final DeleteResult remove = repository.remove(query, ScanerTask.class);
        if(remove.getDeletedCount()==0){
            return ServerResponse.createByError("删除失败");
        }
        return ServerResponse.createBySuccess("删除成功");
    }

    @Override
    public ServerResponse<List<ScanerTask>> TaskList() {
        final List<ScanerTask> all = repository.findAll(ScanerTask.class);
        return ServerResponse.createBySuccess(all);
    }

    @Override
    public ServerResponse<String> Add_Update_Task_Plugs(ScanerTaskGroup params) {

        if(StringUtils.isBlank(params.getGroupNo()) && StringUtils.isBlank(params.getPlugNo())){
            return ServerResponse.createByError("传入的编号参数为空");
        }
        //查询一下当前任务绑定的插件组编号是否正确
        if(StringUtils.isBlank(params.getGroupNo())){
            final ScanerGroup ScanerGroup = repository.findById(params.getGroupNo(), ScanerGroup.class);
            if(ScanerGroup==null){
               return ServerResponse.createByError("传入的插件组编号错误");
            }
        }
        if(StringUtils.isNotBlank(params.getPlugNo())){
            //查询一下当前任务绑定的插件编号是否存在
            final ScanerPlugs scanerPlugs = repository.findById(params.getPlugNo(), ScanerPlugs.class);
            if(scanerPlugs==null){
                return ServerResponse.createByError("传入的插件组编号错误");
            }
        }
        if(StringUtils.isBlank(params.getTaskNo())){
            return ServerResponse.createByError("传入的任务编号参数为空");
        }
        //检查一下任务是否存在
        final ScanerTask taskId = repository.findById(params.getTaskNo(),ScanerTask.class);
        if(taskId==null){
            return ServerResponse.createByError("任务编号不存在");
        }
        params.setCreateTime(LocalDateTime.now());
        final ScanerTaskGroup save = repository.save(params);
        if(save!=null){
            return ServerResponse.createByError("任务组操作失败");
        }
        return ServerResponse.createByError("任务组操作成功");
    }
}
