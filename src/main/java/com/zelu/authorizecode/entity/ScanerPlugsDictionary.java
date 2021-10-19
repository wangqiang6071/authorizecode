package com.zelu.authorizecode.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wangqiang
 * @Date: 2021/10/18 17:40
 */
@Data
@Document(collection = "scaner_plugs_dictionary")
public class ScanerPlugsDictionary implements Serializable {

    @MongoId
    private String dictionaryNo;
    private String taskNo;
    private String dictionaryName;
    private String dictionaryPath;
    private List<String> password;
    private LocalDateTime time;
}
