package com.zelu.authorizecode.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zelu.authorizecode.common.ServerResponse;
import com.zelu.authorizecode.entity.ScanerTaskType;
import com.zelu.authorizecode.service.IScanerTaskTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 任务类型 前端控制器
 * </p>
 * @author wangqiang
 * @since 2021-09-16
 */

@RestController
@RequestMapping("/task_type")
public class ScanerTaskTypeController {

    @Autowired
    private IScanerTaskTypeService taskTypeService;

    //添加
    @PostMapping("add_update")
    public ServerResponse<String> AddTaskType(@RequestBody ScanerTaskType type){
        return taskTypeService.Add_UpdateTaskType(type);
    }
    //删除
    @GetMapping("delete")
    public ServerResponse<String> DeleteTaskType(@RequestParam("type_no") String typeNo){
        return taskTypeService.DeleteTaskType(typeNo);
    }

    //插件
    @PostMapping("list")
    public ServerResponse<List<ScanerTaskType>>  TaskTypeList(){
        return taskTypeService. TaskTypeList();
    }
}
