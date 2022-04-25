package com.pedro.calculomental.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.pedro.calculomental.dtos.ActivityDto;
import com.pedro.calculomental.dtos.UserDto;

@Configuration
@PropertySource("classpath:application.properties")
public class MongoConfig {
	
	@Autowired
	private Environment env;
	
	@Bean
	MongoConnectorDao<UserDto> userDao(){
		return new MongoConnectorDao<>(env.getProperty("collection.users"), UserDto.class);
	}

	@Bean
	MongoConnectorDao<ActivityDto> activitiesDao(){
		return new MongoConnectorDao<>(env.getProperty("collection.activities"), ActivityDto.class);
	}
	
}
