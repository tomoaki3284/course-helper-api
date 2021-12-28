package com.tomoaki3284.coursehelperapi.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.tomoaki3284.coursehelperapi.model.Course;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CoursesService {
	
	@Value("${aws.s3.endpoint.courses}")
	private final String S3_URL;
	
	// this cache's key is redundant since uri is fixed
	// key = uri, val = List of posts that can be fetch from the uri
	// if cache doesn't exist, simply fetch
	private final LoadingCache<String, List<Course>> coursesCache;
	
	public CoursesService(@Value("${aws.s3.endpoint.courses}") final String s3URL) {
		coursesCache = Caffeine.newBuilder()
			.expireAfterWrite(1, TimeUnit.DAYS)
			.build(uri -> getCoursesFromS3(uri));
		
		this.S3_URL = s3URL;
	}
	
	public List<Course> getCourses() {
		return coursesCache.get(S3_URL);
	}
	
	public List<Course> getCoursesFromS3(final String uri) {
		ObjectMapper mapper = new ObjectMapper();
		List<Course> courses = new ArrayList<>();
		try {
			URL url = new URL(uri);
			courses = mapper.readValue(url, new TypeReference<ArrayList<Course>>() {});
			if (courses == null) {
				System.out.println("course is null");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return courses;
	}
}
