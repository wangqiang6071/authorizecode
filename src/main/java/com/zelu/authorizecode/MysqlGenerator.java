package com.zelu.authorizecode;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

/**
 * @author wangqiang
 * @Date: 2021/9/3 10:33
 */
public class MysqlGenerator {
    public static void main(String[] args) {
        AutoGenerator mpg = new AutoGenerator();
        //1、全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");  //生成路径(一般都是生成在此项目的src/main/java下面)
        gc.setAuthor("wangqiang"); //设置作者
        gc.setOpen(false);
        gc.setFileOverride(false); //第二次生成会把第一次生成的覆盖掉
        //gc.setServiceName("%sService"); //生成的service接口名字首字母是否为I，这样设置就没有
        gc.setBaseResultMap(true); //生成resultMap
        mpg.setGlobalConfig(gc);

        //2、数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://172.30.2.28:3309/zelu_scaner_system?useUnicode=true&serverTimezone=GMT&useSSL=false&characterEncoding=utf8");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("PasswordLuzeanquan");
        mpg.setDataSource(dsc);

        // 3、包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("");
        pc.setParent("com.zelu.authorizecode");
        mpg.setPackageInfo(pc);

        // 4、策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setSuperControllerClass("");
        strategy.setSuperEntityClass("");
        // strategy.setTablePrefix("t_"); // 表名前缀
        strategy.setEntityLombokModel(true); //使用lombok
        strategy.setRestControllerStyle(true);
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        strategy.setInclude(new String[]{"scaner_task"});// 逆向工程使用的表   如果要生成多个,这里可以传入String[]
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        //5、执行
        mpg.execute();
    }
}
