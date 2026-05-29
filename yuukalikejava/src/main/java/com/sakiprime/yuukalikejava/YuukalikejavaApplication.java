package com.sakiprime.yuukalikejava;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.sakiprime.yuukalikejava.mapper") // 这里是你的mapper接口所在包，确保路径正确
public class YuukalikejavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(YuukalikejavaApplication.class, args);
	}

}