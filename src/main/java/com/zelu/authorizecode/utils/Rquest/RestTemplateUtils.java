package com.zelu.authorizecode.utils.Rquest;

import com.zelu.authorizecode.entity.ScanerPlugsGroup;
import com.zelu.authorizecode.entity.params.SanerPlugsParams;
import com.zelu.authorizecode.utils.Rquest.entity.RequestModel;
import com.zelu.authorizecode.utils.Rquest.entity.RequestOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangqiang
 * @Date: 2021/9/16 11:46
 */
@Slf4j
public class RestTemplateUtils<T> {

    private RestTemplate restTemplate;
     //实体对象
    private Class<T> clazz;

    public RestTemplateUtils(Class<T> clazz,RestTemplate restTemplate) {
        this.clazz = clazz;
        this.restTemplate=restTemplate;
    }

    public T RestTemplate(String url, String requestType, Map<String,Object> map){
        //创建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //创建请求参数
        HttpEntity<RequestModel> entity = new HttpEntity<>(getParams(requestType,map), headers);
        //发送请求
        ResponseEntity<T> responseEntity = restTemplate.postForEntity(url, entity, clazz);
        return responseEntity.getBody();
    }

    //封装参数
    private RequestModel getParams(String requestType, Map<String,Object> map){
        List<Object> objectList=new ArrayList<>();
        //params类别
        objectList.add(requestType);
        //map以及params参数
        objectList.add(map);
        return new RequestModel("method",objectList);
    }

//    public static void main(String[] args) {
//        List<Object> objectList=new ArrayList<>();
//        objectList.add("plugin_type");
//        Map<String,Object>map=new HashMap<>();
//        map.put("id","bluetooth/fuzz");
//        RequestOption options=new RequestOption();
//        options.setBmac("bmac");
//        options.setDict(new String[]{"dict1","dict2"});
//        options.setSsid("ssid");
//        options.setTimeout(10);
//        map.put("options",options);
//        objectList.add(map);
//        RequestModel json=new RequestModel("method",objectList);
//        System.out.println(json);
//    }

    public static void main(String[] args) {
        List<ScanerPlugsGroup> list = new ArrayList<>();
        // 最终的结果
        //list.get(0).getPlugsNoList().stream().sorted(Comparator.comparing(SanerPlugsParams::getPlugsOrder)).collect(Collectors.toList());
    }

}
