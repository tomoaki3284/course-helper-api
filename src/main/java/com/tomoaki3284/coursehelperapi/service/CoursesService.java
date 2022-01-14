package com.tomoaki3284.coursehelperapi.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.tomoaki3284.coursehelperapi.model.Course;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CoursesService {
	
	@Value("${aws.s3.endpoint.courses}")
	private final String S3_URL;
	
	@Value("${json.key.courses}")
	private final String JSON_KEY_COURSES;
	
	@Value("${json.key.offering-term}")
	private final String JSON_KEY_OFFERING_TERM;
	
	// this cache's key is redundant since uri is fixed
	// key = uri, val = List of posts that can be fetch from the uri
	// if cache doesn't exist, simply fetch
	private final LoadingCache<String, Map<String, Object>> coursesCache;
	
	public CoursesService(
		@Value("${aws.s3.endpoint.courses}") final String s3URL,
		@Value("${json.key.courses}") final String coursesJsonKey,
		@Value("${json.key.offering-term}") final String jsonKeyOfferingTerm
	) {
		coursesCache = Caffeine.newBuilder()
			.expireAfterWrite(1, TimeUnit.DAYS)
			.build(this::getObjectFromS3);
		
		this.S3_URL = s3URL;
		this.JSON_KEY_COURSES = coursesJsonKey;
		this.JSON_KEY_OFFERING_TERM = jsonKeyOfferingTerm;
	}
	
	public List<Course> getCourses() {
		Object obj = coursesCache.get(S3_URL).get(JSON_KEY_COURSES);
		
		List<Course> courses = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			String coursesJsonStr = mapper.writeValueAsString(obj);
			courses = mapper.readValue(coursesJsonStr, new TypeReference<List<Course>>() {});
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return courses;
	}
	
	public String getOfferingTerm() {
		return (String) coursesCache.get(S3_URL).get(JSON_KEY_OFFERING_TERM);
	}
	
	private Map<String,Object> getObjectFromS3(final String uri) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<>();
		try {
			URL url = new URL(uri);
			map = mapper.readValue(url, new TypeReference<HashMap<String, Object>>() {});
			if (map == null) {
				System.out.println("json object is null");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
}
