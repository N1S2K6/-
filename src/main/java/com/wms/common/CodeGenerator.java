package com.wms.common;

import java.util.Scanner;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
// 新版代码生成器 生成后变更：
//       1. XxxMapper.xml文件位置移动到resource的mapper下
//       2. XxxMapper中加注解@Mapper
//       3. Xxx中@Schema改成@ApiModel，name、description改为value，若出现驼峰被识别成下划线分割，则加@Table一系列注解，并将多余的import删去
//       4. IXxxService重构改名为XxxService（非必要）
//       5. XxxService、XxxServiceImpl、XxxMapper、XxxService.Xml根据其他代码作出调整
/**
 * <p>created time:2023/4/25 19:34</p>
 * <p>des:
 *     代码生成器（新）
 * </p>
 *
 * @author Ya Shi
 */
public class CodeGenerator {

    /**
     * 数据源配置
     */
    private static final DataSourceConfig.Builder DATA_SOURCE_CONFIG = new DataSourceConfig
            .Builder("jdbc:mysql://localhost:3306/wms02", "root", "Aa268094")
            .dbQuery(new MySqlQuery());

    /**
     * 输出路径
     */
    private static final String outputDir = System.getProperty("user.dir") + "/wms/src/main/java";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("请输入要生成代码的表名（多个表名用逗号分隔）：");
        String tableNames = scanner.nextLine();


        FastAutoGenerator.create(DATA_SOURCE_CONFIG)
                .globalConfig(builder -> {
                    builder.author("nty") // 设置作者
                            .enableSpringdoc()
//                             .enableSwagger() // 开启 swagger 模式
                            .outputDir(outputDir)   // 指定输出目录
                            .disableOpenDir();   //禁止打开输出目录
                })
                .packageConfig(builder -> {
                    // builder.parent("com.ya.boottest.fruit.autoCode"); // 设置父包名
                    builder.parent("com.wms"); // 设置父包名
                })

                .strategyConfig(builder -> {
                    builder.addInclude(tableNames.split(","))
                            .controllerBuilder().enableFileOverride().enableRestStyle()
                            .serviceBuilder().enableFileOverride()
                            .entityBuilder().enableFileOverride()
                            .mapperBuilder().enableFileOverride(); // 设置需要生成的表名
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}