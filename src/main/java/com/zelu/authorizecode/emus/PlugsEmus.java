package com.zelu.authorizecode.emus;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wangqiang
 * @Date: 2021/6/30 16:59
 */
@Getter
public enum PlugsEmus {
    //基础插件
    Base("base",1),
    //功能插件
    Fuction("fuction",2),
    //蓝牙插件br
    Bluetooth("bluetooth",3),
    Bluetooth_Br("br",5),

    //wife插件
    Wifi("wifi",4),



    ;

    String msg;
    Integer code;

    PlugsEmus(String msg, Integer code) {
        this.code = code;
        this.msg = msg;
    }

    public static Integer getEnumType(String msg) {
        PlugsEmus[] alarmGrades = PlugsEmus.values();
        for (PlugsEmus alarmGrade : alarmGrades) {
            if (StringUtils.equals(alarmGrade.getMsg(),msg)) {
                return alarmGrade.getCode();
            }
        }
        return null;
    }
    //根据code取msg
    public static String getEnumType(Integer code) {
        PlugsEmus[] alarmGrades = PlugsEmus.values();
        for (PlugsEmus alarmGrade : alarmGrades) {
            if (alarmGrade.getCode()==code) {
                return alarmGrade.getMsg();
            }
        }
        return null;
    }
}
