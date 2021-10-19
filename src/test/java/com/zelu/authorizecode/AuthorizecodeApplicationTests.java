package com.zelu.authorizecode;

import com.zelu.authorizecode.dao.AuthortySystemCodeMapper;
import com.zelu.authorizecode.entity.AuthortySystemCode;
import com.zelu.authorizecode.utils.Rquest.RestTemplateUtils;
import com.zelu.authorizecode.utils.Rquest.entity.plugsList.ReturnPlugsListResult;
import com.zelu.authorizecode.utils.Rquest.entity.redisdata.ResultFinalNotCleanData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SpringBootTest
class AuthorizecodeApplicationTests {
    @Autowired
    private AuthortySystemCodeMapper systemCodeMapper;
    @Test
    void contextLoads() {
        //2021-09-06 16:53:34
        LocalDateTime endTime = LocalDateTime.of(2021, 9, 6, 16, 53, 35);
        //LocalDateTime time=LocalDateTime.now();
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String strDate2 = dtf2.format(endTime);
        System.out.println(strDate2);

    }
    @Test
    public void add(){
        Map<String,Object>map=new HashMap<>();
        final List<AuthortySystemCode> systemCodes = systemCodeMapper.selectByMap(map);
        if(systemCodes.size()==0){
            System.out.println("=====");
        }else if(systemCodes.size()>0){
            System.out.println("xxxxx");
        }
    }
    @Test
    public void adds() throws IOException {
        ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(9000));
        serverSocketChannel.configureBlocking(false);
        Selector open = Selector.open();
        serverSocketChannel.register(open, SelectionKey.OP_ACCEPT);
        open.select();
        final Set<SelectionKey> selectionKeys = open.selectedKeys();
        final Iterator<SelectionKey> iterator = selectionKeys.iterator();
        final SelectionKey next = iterator.next();

    }
    @Autowired
    @Qualifier("urlConnection")
    private RestTemplate restTemplate;

    @Test
    public void adds1()  {
        RestTemplateUtils<ReturnPlugsListResult>request=new RestTemplateUtils<>(ReturnPlugsListResult.class,restTemplate);
        Map<String,Object>map=new HashMap<>();
        ReturnPlugsListResult returnPlugsListResult = request.RestTemplate("http://192.168.50.254:5555/interface", "plugin_list", map);
        System.out.println(returnPlugsListResult);
    }
    @Test
    public void adds2()  {
        //RedisTemplate redisTemplate = (RedisTemplate) ApplicationTextUtils.getBeanObj("redisTemplate");
        Set<String>set=new HashSet<>();
        set.add("A1");
        set.add("A2");
        set.add("A3");
        set.add("A4");
//        redisTemplate.opsForSet().add("A","B","C");
//        redisTemplate.opsForSet().add("A",new String[]{"1","2"});
//        final Set a = redisTemplate.opsForSet().members("A");
//        System.out.println("A===>"+a);
        String[] array2 = set.stream().toArray(String[]::new);
        System.out.println("set===>"+array2[0]);
    }
    @Test
    public void adds3()  {
        RestTemplateUtils<ResultFinalNotCleanData>request=new RestTemplateUtils<>(ResultFinalNotCleanData.class,restTemplate);
        Map<String,Object>map=new HashMap<>();
        map.put("task_id","d8c1f970-6e44-48ca-8af3-2860cca61ca3");
        ResultFinalNotCleanData returnPlugsListResult = request.RestTemplate("http://192.168.50.254:5555/interface", "get_task_result", map);
        System.out.println(returnPlugsListResult);
    }
}
