package com.tomoaki3284.coursehelperapi.controller;

import com.tomoaki3284.coursehelperapi.model.Course;
import com.tomoaki3284.coursehelperapi.service.CoursesService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1")
public class CoursesController {
	
	private final CoursesService coursesService;
	
	@Value("${json.key.courses}")
	private final String JSON_KEY_COURSES;
	
	@Value("${json.key.offering-term}")
	private final String JSON_KEY_OFFERING_TERM;
	
	@Autowired
	public CoursesController(
		CoursesService coursesService,
		@Value("${json.key.courses}") final String coursesJsonKey,
		@Value("${json.key.offering-term}") final String jsonKeyOfferingTerm
	) {
		this.coursesService = coursesService;
		this.JSON_KEY_COURSES = coursesJsonKey;
		this.JSON_KEY_OFFERING_TERM = jsonKeyOfferingTerm;
	}
	
	/**
	 * Fetch courses, then map by course title to return it
	 *
	 * @return List of courses group by course title
	 */
	@GetMapping("/courses/group-by-title")
	public Map<String,Object> getCourseGroupByTitle() {
		List<Course> courses = coursesService.getCourses();
		Map<String, List<Course>> map = courses.stream().collect(Collectors.groupingBy(Course::getTitle));
		String offeringTerm = coursesService.getOfferingTerm();
		Map<String, Object> jsonResponse = new HashMap<>();
		jsonResponse.put(JSON_KEY_COURSES, new TreeMap<>(map));
		jsonResponse.put(JSON_KEY_OFFERING_TERM, offeringTerm);
		
		return jsonResponse;
	}
}
