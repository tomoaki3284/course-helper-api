package com.tomoaki3284.coursehelperapi.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomoaki3284.coursehelperapi.model.Course;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CoursesService {
	
	private final String s3URL = "https://coursehelper.s3.amazonaws.com/current-semester.json";
	
	public List<Course> getCoursesFromS3() {
		ObjectMapper mapper = new ObjectMapper();
		List<Course> courses = new ArrayList<>();
		try {
			URL url = new URL(s3URL);
			courses = mapper.readValue(url, new TypeReference<List<Course>>() {});
			if (courses == null) {
				System.out.println("course is null");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return courses;
	}
}
