package com.zelu.authorizecode.utils.Rquest.entity.redisdata;

import com.zelu.authorizecode.entity.params.SanerPlugsRedisDataParams;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 每个插件最后生成的数据
 * @author wangqiang
 * @Date: 2021/9/18 14:25
 */
@Data
@Document(collection = "result_final_fuction_date")
public class ResultFinalFuctionData {

    /**
     * 基础数中搜索目标编号
     */
    @MongoId
    private String targetNo;

    /**
     * redis接口返回值
     */
    private List<SanerPlugsRedisDataParams> params;

    //创建时间
    @CreatedDate
    private LocalDateTime createTime;
}
