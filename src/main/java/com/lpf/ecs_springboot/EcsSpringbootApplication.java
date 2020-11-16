package com.lpf.ecs_springboot;

import com.lpf.ecs_springboot.common.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@ServletComponentScan
public class EcsSpringbootApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(EcsSpringbootApplication.class, args);
        //设置应用程序上下文
        SpringContextUtil.setApplicationContext(applicationContext);

    }

}
