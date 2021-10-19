package com.zelu.authorizecode.utils;

import com.zelu.authorizecode.confige.ApplicationTextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangqiang
 * @Date: 2021/9/2 17:38
 */

@Slf4j
public class MD5Utils {
    //乱码解析
    public static String decodeUTF8Str(String str){
        try {
            return URLDecoder.decode(str.replaceAll("\\\\x", "%"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取ubuntu服务器的uuid
    public static String getUUID() {
        StringBuilder sb = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "dmidecode -t 1 | grep UUID:| awk '{print $2}'"});
            InputStream in = new BufferedInputStream(process.getInputStream());
            int tem = 0;
            byte[] bytes = new byte[1024];
            while ((tem = in.read(bytes)) != -1) {
                sb.append(new String(bytes, 0, tem));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("UUID==>" + sb.toString());
        return sb.toString().trim();
    }

    //获取ubuntu服务器的硬盘号码
    public static String getHardDiskNo() {
        StringBuilder sb = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "dmidecode -t 1 | grep 'VMware-56'| cut -b 17-70"});
            InputStream in = new BufferedInputStream(process.getInputStream());
            int tem = 0;
            byte[] bytes = new byte[1024];
            while ((tem = in.read(bytes)) != -1) {
                sb.append(new String(bytes, 0, tem));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("HardWareNo==>" + sb.toString());
        return sb.toString().trim();
    }

    //封装获取redistemplate的方法
    public static RedisTemplate getRedisTempate() {
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationTextUtils.getBeanObj("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setEnableTransactionSupport(true);//开启事物
        return redisTemplate;
    }

    /**
     * 全字母规则 正整数规则
     */
    public static final String STR_ENG_PATTERN = "^[a-z0-9A-Z]+$";

    public static boolean validateStrEnglish(final String str) {
        if (StringUtils.isEmpty(str)) {
            return Boolean.FALSE;
        }
        boolean matches = str.matches(STR_ENG_PATTERN);
        if (str.length() < 6 || str.length() > 20) {
            return Boolean.FALSE;
        }
        if (matches) {
            return true;
        } else {
            return false;
        }
    }

    //校验手机号
    public static boolean isChinaPhoneLegal(String str) {
        String regExp = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[3,5,6,7,8])" +
                "|(18[0-9])|(19[8,9]))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    //默认格式
    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    //1时间转字符串
    public static String dateToStr(Date date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT);
    }
    //2时间转字符串
    public static String dateToStr(Date date,String FORMAT) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(FORMAT);
    }

    public static String localdateToStr(LocalDateTime time){
        java.time.format.DateTimeFormatter dtf2 = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dtf2.format(time);
    }

    //3字符串转时间
    public static Date strToDate(String dateTimeStr) {
        if(StringUtils.isBlank(dateTimeStr)){
            throw new SecurityException("传入的时间字符串为空");
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

}
