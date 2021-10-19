package com.tomoaki3284.coursehelperapi.config;

import com.tomoaki3284.coursehelperapi.service.CoursesService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
	
	@Bean
	public CoursesService coursesService() {
		return new CoursesService();
	}
}
