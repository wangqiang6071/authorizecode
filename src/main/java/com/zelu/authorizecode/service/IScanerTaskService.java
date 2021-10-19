package com.zelu.authorizecode.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zelu.authorizecode.common.ServerResponse;
import com.zelu.authorizecode.entity.ScanerTask;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zelu.authorizecode.entity.ScanerTaskGroup;
import com.zelu.authorizecode.entity.params.ScanerTaskParams;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * <p>
 * 新建任务 服务类
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-16
 */
public interface IScanerTaskService {
    //新建更新任务
    public ServerResponse<String> Add_Update_Task(ScanerTask params);
    //删除任务
    public ServerResponse<String> DeleteTask(String taskNo);
    //任务列表
    public ServerResponse<List<ScanerTask>> TaskList();

    //任务与插件组或者单个插件 添加-更新
    public ServerResponse<String> Add_Update_Task_Plugs(ScanerTaskGroup params);
}
