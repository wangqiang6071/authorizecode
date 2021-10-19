package com.zelu.authorizecode.controller;

import com.zelu.authorizecode.common.ServerResponse;
import com.zelu.authorizecode.entity.ScanerPlugsDictionary;
import com.zelu.authorizecode.entity.ScanerTask;
import com.zelu.authorizecode.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangqiang
 * @Date: 2021/10/18 17:05
 */
@Slf4j
@RestController
@RequestMapping("/file")
public class FileUploadingController {

    //文件存放地址
    @Value("${web.resource-path}")
    private String filePath;

    @Autowired
    private MongoTemplate repository;

    @PostMapping("upload")
    public ServerResponse<String> upload_file(MultipartFile file, String taskNo,String dictionaryName){
        if(file==null){
            return ServerResponse.createByError("上传文件为空");
        }
        if(StringUtils.isBlank(dictionaryName)){
            return ServerResponse.createByError("传入的字典的名字不能为空");
        }
        if(StringUtils.isBlank(taskNo)){
            return ServerResponse.createByError("传入的任务编号不能为空");
        }
        final ScanerTask Task = repository.findById(taskNo, ScanerTask.class);
        if(Task==null){
            return ServerResponse.createByError("任务编号不能为空");
        }
        Criteria criteria = new Criteria();
        criteria.andOperator(criteria.where("dictionary_name").is(dictionaryName));
        Query query = new Query(criteria);
        ScanerPlugsDictionary dictionary = repository.findOne(query,ScanerPlugsDictionary.class);
        if(dictionary!=null){
            return ServerResponse.createByError("字典的名字已存在");
        }
        //字典存放路径
        final String FilePath = JWTUtils.uploadFile(file, filePath);
        ScanerPlugsDictionary newdictionary=new ScanerPlugsDictionary();
        newdictionary.setTime(LocalDateTime.now());
        newdictionary.setTaskNo(taskNo);
        newdictionary.setDictionaryName(dictionaryName);
        newdictionary.setDictionaryPath(FilePath);
        newdictionary.setDictionaryNo(JWTUtils.getStringId());
        List<String> passwordList=new ArrayList<>();
        BufferedReader input=null;
        try {
            log.info("文件地址===》"+FilePath);
            input=new BufferedReader(new InputStreamReader(new FileInputStream(FilePath)));
            String str="";
            while (StringUtils.isNotBlank(str=input.readLine())) {
                log.info("password==>"+str);
                passwordList.add(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(input!=null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        newdictionary.setPassword(passwordList);
        final ScanerPlugsDictionary save = repository.save(newdictionary);
        if(save==null){
            return ServerResponse.createByError("字典上传失败");
        }
        return ServerResponse.createBySuccess("字典上传成功");
    }

    //字典名字列表
    @GetMapping("name_list")
    public ServerResponse<List<ScanerPlugsDictionary>>PlugsDictionaryList(){
        final List<ScanerPlugsDictionary> alls = repository.findAll(ScanerPlugsDictionary.class);
        List<ScanerPlugsDictionary>listName=new ArrayList<>();
        if(alls==null){
            return ServerResponse.createByError("暂无字典列表");
        }else if(alls.size()==0){
            return ServerResponse.createByError("暂无字典列表");
        }else{
            for(ScanerPlugsDictionary all:alls){
                ScanerPlugsDictionary dictionary=new ScanerPlugsDictionary();
                dictionary.setDictionaryNo(all.getDictionaryNo());
                dictionary.setDictionaryName(all.getDictionaryName());
                listName.add(dictionary);
            }
        }
        return ServerResponse.createBySuccess(listName);
    }

    //根据字典编号查询对应的字典数据
    @GetMapping("password_list")
    public ServerResponse<List<String>>PlugsDictionaryList(String dictionaryNo){
        final ScanerPlugsDictionary dictionary = repository.findById(dictionaryNo,ScanerPlugsDictionary.class);
        if(dictionary==null){
            return ServerResponse.createByError("传入的字典编号不存在");
        }
        return ServerResponse.createBySuccess(dictionary.getPassword());
    }
}
