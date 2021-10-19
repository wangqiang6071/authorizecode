package com.zelu.authorizecode.controller;

import com.zelu.authorizecode.common.ServerResponse;
import com.zelu.authorizecode.entity.ScanerGroup;
import com.zelu.authorizecode.service.IScanerGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * <p>
 * 插件组 前端控制器
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-16
 */
@RestController
@RequestMapping("/group")
public class ScanerGroupController {
    @Autowired
    private IScanerGroupService groupService;
    //添加
    @PostMapping("add_update")
    public ServerResponse<String> AddGroup(@RequestBody ScanerGroup group){
        return groupService.add_update_plug_group(group);
    }
    //删除
    @GetMapping("delete")
    public ServerResponse<String> DeleteGroup(@RequestParam("group_no") String groupNo){
        return groupService.delete_plug_group(groupNo);
    }

    //列表
    @PostMapping("list")
    public ServerResponse<List<ScanerGroup>> GroupList(){
        return groupService.plugs_group_list();
    }
}
