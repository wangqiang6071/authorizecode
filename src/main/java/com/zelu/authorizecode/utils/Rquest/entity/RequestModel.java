package com.zelu.authorizecode.utils.Rquest.entity;

import lombok.Data;
import java.util.List;

/**
 * @author wangqiang
 * @Date: 2021/9/15 11:00
 * 接口请求参数的实体对象1
 */
@Data
public class RequestModel {
   private String func;//默认为method
   private List<Object> params;//数组参数[String,RequestObj对象]
   public RequestModel(){}
   public RequestModel(String func,List<Object> params){
      this.func=func;
      this.params=params;
   }
}
