package com.example.demo.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;

import java.util.Collections;

/***
 * mybatis-plus 代码生成器
 *
 */

// https://baomidou.com/pages/779a6e/#%E4%BD%BF%E7%94%A8
public class CodeGenerator {

    public static void main(String[] args) {
//        generate("sys_user");
//        generate("sys_file");
//        generate("sys_role");
//        generate("sys_menu");
//        generate("sys_dict");
//        generate("sys_role_menu");


    }


    private static void generate(String tableName) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/onlinejudge?serverTimezone=GMT%2b8", "root", "")
                .globalConfig(builder -> {
                    builder
//                            .author("zime") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("D:\\毕设\\demo\\src\\main\\java\\"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.example.demo") // 设置父包名
                            .moduleName(null) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "D:\\毕设\\demo\\src\\main\\resources\\mapper\\")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder().enableLombok(); // 开启Lombok模式
//                    builder.mapperBuilder().enableMapperAnnotation().build(); // 是否在mapper文件夹下每一个mapper中添加 @Mapper 注解
                    builder.controllerBuilder().enableHyphenStyle()  // 开启驼峰转连字符
                            .enableRestStyle();  // 开启生成@RestController 控制器
                    builder.addInclude(tableName) // 设置需要生成的表名
                            .addTablePrefix("t_", "sys_"); // 设置过滤表前缀
                })
//                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

}
