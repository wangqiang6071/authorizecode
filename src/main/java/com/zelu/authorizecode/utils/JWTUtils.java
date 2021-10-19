package com.zelu.authorizecode.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zelu.authorizecode.entity.params.AuthortyCodeParams;
import com.zelu.authorizecode.exceptions.StringException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangqiang
 * @Date: 2021/9/2 17:37
 */
@Slf4j
public class JWTUtils {

    //Base64编码
    public static String encodeBase64(String key){
        //Base64
        return Base64.getEncoder().encodeToString(key.getBytes(StandardCharsets.UTF_8));
    }
    //Base64解码
    public static String decodeBase64(String key){
        return new String(Base64.getDecoder().decode(key.getBytes(StandardCharsets.UTF_8)));
    }

    //1生成密钥
    public static String getSercrtKey(int len){
        final String stringId = getStringId();
        String charry="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890~!@#$%^&*.?";
        String ret=stringId+charry;
        char charr[] = ret.toCharArray();
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        for (int x = 1; x < len; x++) {
            sb.append(charr[r.nextInt(charr.length)]);
            if(x!=len-1){
                sb.append(x%5==0?"-":"");
            }
        }
        return sb.toString();
    }

    //2生成授权码
    public static String getAuthortyNo(AuthortyCodeParams params){
        String modelNo=params.getModelNo().stream().filter(Str->!Str.isEmpty()).collect(Collectors.joining("-"));
        //long minutes = ChronoUnit.MINUTES.between(Instant.ofEpochMilli(new Date().getTime()), Instant.ofEpochMilli(params.getPeriodofValidity().getTime()));
        final LocalDateTime now = LocalDateTime.now();
        if(params.getPeriodofValidity().isBefore(now)){
            throw new StringException("授权时间不能晚于当前时间");
        }
        //计算当前时间距离过期时间相差多少时间
        Duration duration = Duration.between(now,params.getPeriodofValidity());
        log.info(String.format("使用后的授权模块:%s,授去时间%s,与当前时间相差%s分钟",modelNo,params.getPeriodofValidity(),duration.toMinutes()));
        //设置超时时间
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE,Integer.parseInt(String.valueOf(duration.toMinutes())));
        //设置加密规则
        Map<String,Object> map=new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        //生成token
        return JWT.create().withHeader(map)
                .withClaim("SystemUUID",addMd5Hash(params.getSystemUuid(),params.getRandom1(),params.getSalt1()))
                .withClaim("SystemDiskNo",addMd5Hash(params.getSystemDiskno(),params.getRandom2(),params.getSalt2()))
                .withClaim("CompanyName",params.getCompanyName())
                .withClaim("modelNo",modelNo)
                //.withClaim("expiresAt",MD5Utils.localdateToStr(params.getPeriodofValidity()))
                .withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(addMd5Hash(params.getAuthortyKey(),params.getRandom(),params.getSalt())));
    }

    //3解析授权码
    public static Map<String, Claim> ParaseSerctNo(String SerctNo,String key,int random,String salt){
        if(StringUtils.isBlank(SerctNo)){
            throw new StringException("填入的授权码不能为空");
        }
        if(StringUtils.isBlank(key)){
            throw new StringException("填入的密钥不能为空");
        }
        JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(addMd5Hash(key,random,salt))).build();
        DecodedJWT verify = jwtVerifier.verify(SerctNo);
        return verify.getClaims();
    }

    // 4MD5加密+盐（用户名）+散列次数
    public static String addMd5Hash(String sercrty,int random,String salt) {
        return new Md5Hash(sercrty, salt, random).toString();
    }

    //5生成唯一的id
    public static String getStringId(){
        Long random = UUID.randomUUID().getMostSignificantBits();
        long ane=random+new Date().getTime();
        Long id = Math.abs(ane);
        return id.toString();
    }

    //时间延后多少小时
    public static Date addDateMinut(Date date , int hour){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);// 24小时制
        return cal.getTime();
    }



    /**
     * @param file 文件流 压缩包
     */
    public static String uploadFile(MultipartFile file,String basedir) {
        //文件的类型
        String fileType = file.getContentType();
        log.info("fileType==>"+fileType);
        if(isZipFile(fileType)){
            //文件的原始名字
            String fileOldName = file.getOriginalFilename();
            //获取文件的后缀名
            String oldExtent="."+ FilenameUtils.getExtension(fileOldName);
            //新文件名字
            String newFileName= UUID.randomUUID().toString().replace("-","")+oldExtent;
            //文件存放路径
            String realPath2="";
            try {
                String realPath1=MD5Utils.dateToStr(new Date(),"yyyyMMdd")+"/";
                realPath2=basedir+realPath1;
                //创建文件路径
                File file1=new File(realPath2);
                if(!file1.exists()){
                    file1.mkdirs();
                }
                file.transferTo(new File(realPath2,newFileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return realPath2+newFileName;
        }
        throw new StringException("非法文件格式");
    }


    //判断是否是文件
    private static boolean isZipFile(String filetype){
        List<String> list=new ArrayList();
        list.add("application/zip");
        list.add("application/json");
        list.add("application/x-zip-compressed");
        list.add("application/x-rar-compressed");
        list.add("application/octet-stream");
        list.add("text/html");
        return list.contains(filetype);
    }
}