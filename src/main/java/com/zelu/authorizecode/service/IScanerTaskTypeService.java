package com.zelu.authorizecode.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zelu.authorizecode.common.ServerResponse;
import com.zelu.authorizecode.entity.ScanerTaskType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zelu.authorizecode.entity.params.ScanerTaskParams;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * <p>
 * 任务类型 服务类
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-16
 */
public interface IScanerTaskTypeService {
    //新建更新任务类型
    public ServerResponse<String> Add_UpdateTaskType(ScanerTaskType type);
    //删除任务类型
    public ServerResponse<String> DeleteTaskType(String typeNo);
    //任务类型列表
    public ServerResponse<List<ScanerTaskType>> TaskTypeList();
}
