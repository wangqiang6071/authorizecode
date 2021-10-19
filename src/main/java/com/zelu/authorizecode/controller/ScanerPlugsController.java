package com.zelu.authorizecode.controller;

import com.zelu.authorizecode.common.ServerResponse;
import com.zelu.authorizecode.entity.ScanerPlugs;
import com.zelu.authorizecode.entity.ScanerPlugsParams;
import com.zelu.authorizecode.service.IScanerPlugsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * <p>
 * 所有插件列表 前端控制器
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-16
 */
@RestController
@RequestMapping("/plugs")
public class ScanerPlugsController {
    @Autowired
    private IScanerPlugsService plugsService;

    //添加更新插件
    @PostMapping("add_update")
    public ServerResponse<String> AddPlugs(@RequestBody ScanerPlugs params){
        return plugsService.add_update_plugs(params);
    }
    //删除插件=查询下面是否有数据
    @GetMapping("delete")
    public ServerResponse<String> DeletePlugs(@RequestParam("plugs_no") String plugsNo){
        return plugsService.delete_plugs(plugsNo);
    }

    //插件列表
    @GetMapping("list")
    public ServerResponse<List<ScanerPlugs>> PlugsList(){
        return plugsService.plugs_list();
    }

    //添加更新插件参数
    @PostMapping("add_update_params")
    public ServerResponse<String> UpdatePlugsParams(@RequestBody ScanerPlugsParams params){
        return plugsService.add_update_plugs_params(params);
    }
    //删除插件参数
    @GetMapping("delete_params")
    public ServerResponse<String> DeletePlugsParams(@RequestParam("plugs_no") String plugsNo){
        return plugsService.delete_plugs_params(plugsNo);
    }
    //获取插件参数
    @GetMapping("get_params")
    public ServerResponse<ScanerPlugsParams> GetPlugsParams(@RequestParam("plugs_no") String plugsNo){
        return plugsService.get_plugs_params_by_plugno(plugsNo);
    }
}
