package com.zelu.authorizecode.controller;


import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zelu.authorizecode.common.ResponseCode;
import com.zelu.authorizecode.common.ServerResponse;
import com.zelu.authorizecode.dao.*;
import com.zelu.authorizecode.entity.*;
import com.zelu.authorizecode.entity.params.AuthortyActivationParams;
import com.zelu.authorizecode.entity.params.AuthortyCodeParams;
import com.zelu.authorizecode.exceptions.StringException;
import com.zelu.authorizecode.utils.JWTUtils;
import com.zelu.authorizecode.utils.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import sun.security.action.GetPropertyAction;
import java.security.AccessController;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 授权码表 系统私有表 前端控制器
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-03
 */
@Slf4j
@RestController
@RequestMapping("/code")
public class AuthortyCodeController {

    @Value("${shiro.password.salt}")
    private String salt;
    @Value("${shiro.password.random}")
    private int random;

    @Value("${shiro.password.salt1}")
    private String salt1;
    @Value("${shiro.password.random1}")
    private int random1;

    @Value("${shiro.password.salt2}")
    private String salt2;
    @Value("${shiro.password.random2}")
    private int random2;

    @Value("${shiro.password.lenth}")
    private int keylenth;

    @Autowired
    private AuthortyModelMapper modelMapper;
    @Autowired
    private AuthortyCodeMapper codeMapper;
    @Autowired
    private AuthortySystemCodeMapper systemCodeMapper;
    @Autowired
    private AuthortySystemBindModelMapper systemBindModelMapper;
    @Autowired
    private AuthortyBindModelMapper codeModelMapper;

    //授权码和密钥的生成
    @ResponseBody
    @Transactional
    @PostMapping("generate_code")
    public ServerResponse<String> GenerateCodeAndSercty(@RequestBody AuthortyCodeParams params){
        if(StringUtils.isBlank(params.getCompanyName())){
            return ServerResponse.createByError("公司的名字不能为空");
        }
        //0 获取服务器的唯一标识
        final String uuid = params.getSystemUuid();
        //1 获取服务器的硬盘编号
        final String hardDiskNo = params.getSystemDiskno();
        if(StringUtils.isBlank(uuid)){
            ServerResponse.createByError("服务器的唯一标识不能为空");
        }
        if(StringUtils.isBlank(hardDiskNo)){
            ServerResponse.createByError("服务器的硬盘编号不能为空");
        }
        log.info(String.format("系统唯一编号:%s,系统硬盘编号:%s",uuid,hardDiskNo));
        //检查是否存在有效期内重复的授权码
        Map<String,Object>maps=new HashMap<>();
        maps.put("system_uuid",uuid);
        maps.put("system_diskno",hardDiskNo);
        LocalDateTime nowTime= LocalDateTime.now();
        final List<AuthortyCode> authortyCodes = codeMapper.selectByMap(maps);
        final List<AuthortyCode> limit = authortyCodes.stream().filter(a -> a.getPeriodofValidity().isAfter(nowTime)).collect(Collectors.toList());
        if(limit.size()>0){
            return ServerResponse.createByError("当前服务器已存在有效的授权码",JWTUtils.encodeBase64(limit.get(0).getAuthortyCode()),JWTUtils.encodeBase64(limit.get(0).getAuthortyKey()));
        }
        //1 生成密钥key
        final String authortyKey = JWTUtils.getSercrtKey(keylenth);
        //2 根据密钥生成授权码
        params.setAuthortyKey(authortyKey);
        params.setRandom(random);
        params.setSalt(salt);
        params.setRandom1(random1);
        params.setSalt1(salt1);
        params.setRandom2(random2);
        params.setSalt2(salt2);
        params.setSystemDiskno(hardDiskNo);
        params.setSystemUuid(uuid);
        log.info(String.format("生成的密钥:%s,密钥的长度:%s",JWTUtils.encodeBase64(authortyKey),keylenth));
        //随机编号授权码
        final String stringId = JWTUtils.getStringId();
        //授权的模块编号
        log.info("params.getModelNo()==>"+params.getModelNo());
        if(params.getModelNo().size()!=0){
            for(String no:params.getModelNo()){
                QueryWrapper<AuthortyModel>queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("model_no",no);
                if(modelMapper.selectOne(queryWrapper)==null){
                    throw new StringException("传入的模块编号有误");
                }
                //授权的模块存入数据库
                AuthortyBindModel codemodel=new AuthortyBindModel();
                codemodel.setBindNo(JWTUtils.getStringId());
                //随机编号授权码
                codemodel.setAuthortyNo(stringId);
                //模块编号
                codemodel.setModelNo(no);
                if(codeModelMapper.insert(codemodel)==0){
                    throw new StringException("存入激活码和授权模块失败");
                }
            }
        }else{
            throw new StringException("授权模块不能空");
        }
        //生成授权码
        final String authortyCode = JWTUtils.getAuthortyNo(params);
        log.info(String.format("生成的授权码:%s,传入的授权模块:%s",authortyCode,params.getModelNo()));
        //授权码和密钥存入数据库
        AuthortyCode code=new AuthortyCode();
        BeanUtils.copyProperties(params,code);
        //随机编号授权码
        code.setAuthortyNo(stringId);
        //授权码编号
        code.setAuthortyCode(authortyCode);
        final int insert = codeMapper.insert(code);
        if(insert==0){
            return ServerResponse.createByError("生成授权码失败");
        }
        return ServerResponse.createBySuccess(JWTUtils.encodeBase64(authortyCode),JWTUtils.encodeBase64(authortyKey));
    }



    //服务器端

    //检查服务端是否被授权和授权码是否过期
    @ResponseBody
    @Transactional
    @GetMapping("check_expired")
    public ServerResponse<String>CheckIsExpired(){
        Map<String,Object>map=new HashMap<>();
        final List<AuthortySystemCode> systemCodes = systemCodeMapper.selectByMap(map);
        if(systemCodes.size()==0){
            //不存在 直接进行授权 保存授权码和密钥
            return ServerResponse.createByError("服务器未被激活");
        }else if(systemCodes.size()>0){
            try {
                //存在 检查授权码的有效时间是否过期
                JWTUtils.ParaseSerctNo(systemCodes.get(0).getAuthortyCode(), systemCodes.get(0).getAuthortyKey(), random, salt);
                return ServerResponse.createBySuccess("服务器已激活");
            }catch (TokenExpiredException e){
                //已过期
                return ServerResponse.createByError(ResponseCode.ILLEGAL_String,"被激活的服务器已过期");
            }
        }
        throw new StringException("服务器激活发生异常");
    }

    //进行给服务器授权=解密
    @ResponseBody
    @Transactional
    @PostMapping("activation_service")
    public ServerResponse<String> ActivationService(@RequestBody AuthortyActivationParams params) {
        //已经激活0
        if(StringUtils.equals(ResponseCode.getEnumType(params.getType()),ResponseCode.SUCCESS.getMsg())){
            QueryWrapper<AuthortySystemCode>queryWrappers=new QueryWrapper<>();
            queryWrappers.eq("authorty_key",JWTUtils.decodeBase64(params.getSercrtKey()));
            queryWrappers.eq("authorty_code",JWTUtils.decodeBase64(params.getAuthortyCode()));
            final AuthortySystemCode systemCode1 = systemCodeMapper.selectOne(queryWrappers);
            if(systemCode1!=null){
                throw new StringException("当前的密钥和授权码已被使用");
            }
            return ServerResponse.createByError("无需重复激活");
         //未激活-1
        }else if(StringUtils.equals(ResponseCode.getEnumType(params.getType()),ResponseCode.ERROR.getMsg())){
            final Map<String, Claim> claimMap = JWTUtils.ParaseSerctNo(params.getAuthortyCode(), params.getSercrtKey(), random, salt);
            QueryWrapper<AuthortySystemCode>queryWrappers=new QueryWrapper<>();
            queryWrappers.eq("authorty_key",JWTUtils.decodeBase64(params.getSercrtKey()));
            queryWrappers.eq("authorty_code",JWTUtils.decodeBase64(params.getAuthortyCode()));
            final AuthortySystemCode systemCode1 = systemCodeMapper.selectOne(queryWrappers);
            if(systemCode1!=null){
                throw new StringException("当前的密钥和授权码已被使用");
            }
            //获取现在准备激活的服务器的属性值
            //获取服务器的uuid
            if(!StringUtils.equals(JWTUtils.addMd5Hash(MD5Utils.getUUID(),random1,salt1),claimMap.get("SystemUUID").asString())){
                return ServerResponse.createByError("激活码中的uuid与服务器的uuid不一致");
            }
            //获取服务器的硬盘编号
            if(!StringUtils.equals(JWTUtils.addMd5Hash(MD5Utils.getHardDiskNo(),random2,salt2),claimMap.get("SystemDiskNo").asString())){
                return ServerResponse.createByError("激活码中的硬盘编号与服务器的硬盘编号不一致");
            }
            //获取授权模块
            final String[] modelNos = claimMap.get("modelNo").asString().split("-");
            final String stringId = JWTUtils.getStringId();
            for(String model_no:modelNos){
                QueryWrapper<AuthortyModel>queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("model_no",model_no);
                final AuthortyModel authortyModel = modelMapper.selectOne(queryWrapper);
                if(authortyModel==null){
                    throw new StringException("授权模块编号不存在");
                }
                AuthortySystemBindModel systemBindModel=new AuthortySystemBindModel();
                systemBindModel.setBindNo(JWTUtils.getStringId());
                //授权的激活码的编号
                systemBindModel.setAuthortyNo(stringId);
                systemBindModel.setModelNo(model_no);
                final int insert = systemBindModelMapper.insert(systemBindModel);
                if(insert==0){
                    throw new StringException("激活码和模块绑定失败");
                }
            }
            //授权码加入到数据库
            AuthortySystemCode systemCode=new AuthortySystemCode();
            systemCode.setAuthortyNo(stringId);
            systemCode.setAuthortyCode(params.getAuthortyCode());
            systemCode.setAuthortyKey(params.getSercrtKey());
            final int insert = systemCodeMapper.insert(systemCode);
            if(insert==0){
                throw new StringException("授权码激活失败");
            }
            return ServerResponse.createBySuccess("授权码激活成功");
            //激活已过期-2
        }else if(StringUtils.equals(ResponseCode.getEnumType(params.getType()),ResponseCode.ILLEGAL_String.getMsg())){
            //检查当前的授权码是否被使用过
            QueryWrapper<AuthortySystemCode>queryWrappers=new QueryWrapper<>();
            queryWrappers.eq("authorty_key",JWTUtils.decodeBase64(params.getSercrtKey()));
            queryWrappers.eq("authorty_code",JWTUtils.decodeBase64(params.getAuthortyCode()));
            final AuthortySystemCode systemCode1 = systemCodeMapper.selectOne(queryWrappers);
            if(systemCode1!=null){
                throw new StringException("当前的密钥和授权码已被使用");
            }
            //1删除之前的授权模块
            Map<String,Object>map=new HashMap<>();
            final List<AuthortySystemCode> systemCodes = systemCodeMapper.selectByMap(map);
            if(systemCodes.size()==0){
                throw new StringException("之前服务器不存在激活码");
            }
            //删除之前授权码绑定的模块
            final AuthortySystemCode systemCode = systemCodes.get(0);
            Map<String,Object>maps=new HashMap<>();
            maps.put("authorty_no",systemCode.getAuthortyNo());
            systemBindModelMapper.deleteByMap(maps);
            //检查传入的激活码是否正确
            final Map<String, Claim> claimMap = JWTUtils.ParaseSerctNo(params.getAuthortyCode(), params.getSercrtKey(), random, salt);
            //2获取现在准备激活的服务器的属性值
            //获取服务器的uuid
            if(!StringUtils.equals(JWTUtils.addMd5Hash(MD5Utils.getUUID(),random1,salt1),claimMap.get("SystemUUID").asString())){
                return ServerResponse.createByError("激活码中的uuid与服务器的uuid不一致");
            }
            //获取服务器的硬盘编号
            if(!StringUtils.equals(JWTUtils.addMd5Hash(MD5Utils.getHardDiskNo(),random2,salt2),claimMap.get("SystemDiskNo").asString())){
                return ServerResponse.createByError("激活码中的硬盘编号与服务器的硬盘编号不一致");
            }
            final String[] modelNos = claimMap.get("modelNo").asString().split("-");
            for(String model_no:modelNos){
                QueryWrapper<AuthortyModel>queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("model_no",model_no);
                final AuthortyModel authortyModel = modelMapper.selectOne(queryWrapper);
                if(authortyModel==null){
                    throw new StringException("授权模块编号不存在");
                }
                AuthortySystemBindModel systemBindModel=new AuthortySystemBindModel();
                systemBindModel.setBindNo(JWTUtils.getStringId());
                //授权的激活码的编号
                systemBindModel.setAuthortyNo(systemCode.getAuthortyNo());
                systemBindModel.setModelNo(model_no);
                final int insert = systemBindModelMapper.insert(systemBindModel);
                if(insert==0){
                    throw new StringException("激活码和模块绑定失败");
                }
            }
            //更新授权码
            systemCode.setAuthortyCode(params.getAuthortyCode());
            systemCode.setAuthortyKey(params.getSercrtKey());
            systemCodeMapper.updateById(systemCode);
            return ServerResponse.createBySuccess("授权码激活成功");
        }
        throw new StringException("传入的激活状态不正确");
    }
}
