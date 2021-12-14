package com.tomoaki3284.coursehelperapi.controller;

import com.tomoaki3284.coursehelperapi.model.Course;
import com.tomoaki3284.coursehelperapi.service.CoursesService;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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
	
	/**
	 * Fetch courses by utilizing coursesService method, and return the result
	 *
	 * @return List<Course> list of courses
	 * @throws InterruptedException
	 */
	@CrossOrigin
	@GetMapping("/courses")
	public List getCourses() throws InterruptedException {
		List<Course> courses = coursesService.getCourses();
		return courses;
	}
	
	/**
	 * Fetch courses, then map by course title to return it
	 *
	 * @return List of courses group by course title
	 */
	@CrossOrigin
	@GetMapping("/courses/group-by-title")
	public Map<String,List<Course>> getCourseGroupByTitle() {
		List<Course> courses = coursesService.getCourses();
		Map<String, List<Course>> map = courses.stream().collect(Collectors.groupingBy(Course::getTitle));
		map = new TreeMap<>(map);
		return map;
	}
}
