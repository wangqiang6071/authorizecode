package com.zelu.authorizecode.controller;

import com.zelu.authorizecode.common.ServerResponse;
import com.zelu.authorizecode.entity.ScanerTask;
import com.zelu.authorizecode.entity.ScanerTaskGroup;
import com.zelu.authorizecode.entity.params.ScanerTaskParams;
import com.zelu.authorizecode.service.IScanerTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * <p>
 * 新建任务 前端控制器
 * </p>
 * @author wangqiang
 * @since 2021-09-16
 */
@RestController
@RequestMapping("/task")
public class ScanerTaskController {
    @Autowired
    private IScanerTaskService taskService;

    //添加/更新
    @PostMapping("add_update_task")
    public ServerResponse<String> AddUpdateTask(@RequestBody ScanerTask params){
        return taskService.Add_Update_Task(params);
    }
    //删除
    @GetMapping("delete")
    public ServerResponse<String> DeleteTask(@RequestParam("task_no") String taskNo){
        return taskService.DeleteTask(taskNo);
    }
    //列表
    @PostMapping("list")
    public ServerResponse<List<ScanerTask>> PlugsList(){
        return taskService.TaskList();
    }

    //任务与插件组或者单个插件 添加-更新
    @PostMapping("add_update_task_group")
    public ServerResponse<String> AddUpdateTaskPlugs(@RequestBody ScanerTaskGroup params){
        return taskService.Add_Update_Task_Plugs(params);
    }
}
