package com.zelu.authorizecode.controller;

import com.zelu.authorizecode.common.ServerResponse;
import com.zelu.authorizecode.entity.ScanerPlugs;
import com.zelu.authorizecode.entity.ScanerPlugsGroup;
import com.zelu.authorizecode.entity.params.SanerPlugsParams;
import com.zelu.authorizecode.service.IScanerPlugsGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * <p>
 *  插件与插件组的关系 前端控制器
 * </p>
 * @author wangqiang
 * @since 2021-09-16
 */
@RestController
@RequestMapping("/plugs_group")
public class ScanerPlugsGroupController {
    @Autowired
    private IScanerPlugsGroupService plugsGroupService;

    //插件组添加插件
    @PostMapping("add_update")
    public ServerResponse<String> AddUpdateGroupPlugs(@RequestBody ScanerPlugsGroup params){
        return plugsGroupService.add_update_plug_group(params);
    }

    //插件组删除插件=查询下面是否有数据
    @GetMapping("delete")
    public ServerResponse<String> DeletePlugs(@RequestParam("group_no") String gropNo){
        return plugsGroupService.delete_plug_group(gropNo);
    }

    //插件组中的插件列表
    @PostMapping("details")
    public ServerResponse<List<SanerPlugsParams>> PlugsGroupDetails(@RequestParam("grop_no") String groupNo){
        return plugsGroupService.get_plug_group_details(groupNo);
    }
}
