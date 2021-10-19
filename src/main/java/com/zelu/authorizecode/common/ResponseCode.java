package com.zelu.authorizecode.common;

import lombok.Getter;

@Getter
public enum ResponseCode {
	
	// 枚举方法
	ERROR(-1, "服务器未被激活"),

	SUCCESS(0, "服务器已激活"),
	
	ILLEGAL_ARGUMENT(-11, "ILLEGAL_ARGUMENT"),// 非法参数
	ILLEGAL_String(-2,"服务器已激活但已过期"),


	;

	// 属性值
	private final Integer code;
	private final String msg;

	// 构造器
	ResponseCode(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	//根据枚举类的
	public static String getEnumType(Integer code) {
		ResponseCode[] alarmGrades = ResponseCode.values();
        for (ResponseCode alarmGrade : alarmGrades) {
            if (alarmGrade.getCode().equals(code)) {
                return alarmGrade.getMsg();
            }
        }
        return null;
    }

	public static void main(String[] args) {
		final String enumType = ResponseCode.getEnumType(0);
		System.out.println(enumType);
	}
}
