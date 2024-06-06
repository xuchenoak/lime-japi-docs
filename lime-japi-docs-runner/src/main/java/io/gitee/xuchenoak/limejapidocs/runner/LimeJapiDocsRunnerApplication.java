package io.gitee.xuchenoak.limejapidocs.runner;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 启动类
 *
 * @author xuchenoak
 **/
@EnableAsync
@SpringBootApplication
@MapperScan("io.gitee.xuchenoak.limejapidocs.runner.mapper")
public class LimeJapiDocsRunnerApplication {

    private static final Logger logger = LoggerFactory.getLogger(LimeJapiDocsRunnerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(LimeJapiDocsRunnerApplication.class, args);
        logger.info("lime-japi-docs-runner 启动成功！");
    }

}
