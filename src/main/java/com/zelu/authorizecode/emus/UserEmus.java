package com.zelu.authorizecode.emus;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wangqiang
 * @Date: 2021/6/30 16:59
 */
@Getter
public enum UserEmus {
    //可用
    allow("可用",1),
    //禁用
    forbidden("禁用",2),

    ;

    String msg;
    Integer code;

    UserEmus(String msg,Integer code) {
        this.code = code;
        this.msg = msg;
    }

    public static Integer getEnumType(String msg) {
        UserEmus[] alarmGrades = UserEmus.values();
        for (UserEmus alarmGrade : alarmGrades) {
            if (StringUtils.equals(alarmGrade.getMsg(),msg)) {
                return alarmGrade.getCode();
            }
        }
        return null;
    }
    //根据code取msg
    public static String getEnumType(Integer code) {
        UserEmus[] alarmGrades = UserEmus.values();
        for (UserEmus alarmGrade : alarmGrades) {
            if (alarmGrade.getCode()==code) {
                return alarmGrade.getMsg();
            }
        }
        return null;
    }
}
