package com.tomoaki3284.coursehelperapi.controller;

import com.tomoaki3284.coursehelperapi.model.Course;
import com.tomoaki3284.coursehelperapi.service.CoursesService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CoursesController {
	
	private final CoursesService coursesService;
	
	@Autowired
	public CoursesController(CoursesService coursesService) {
		this.coursesService = coursesService;
	}
	
	@GetMapping("/courses")
	public List getCourses() {
		// todo: fetch courses from service class and return it
		List<Course> courses = coursesService.getCoursesFromS3();
		return courses;
	}
}
