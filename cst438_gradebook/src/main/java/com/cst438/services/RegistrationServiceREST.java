package com.cst438.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import com.cst438.domain.CourseDTOG;

public class RegistrationServiceREST extends RegistrationService {

	
	RestTemplate restTemplate = new RestTemplate();
	
	@Value("${registration.url}") 
	String registration_url;
	
	public RegistrationServiceREST() {
		System.out.println("REST registration service ");
	}
	
	@Override
	public void sendFinalGrades(int course_id , CourseDTOG courseDTO) { 
		
		//printing out a message for the console
		System.out.println("Sending final grades" + course_id + " " + courseDTO);
		
		//put call to registration URL. appending parameters to end of localhost url
		restTemplate.put(registration_url + "/course/" + course_id, courseDTO);
		
		//another console message for debugging
		System.out.println("After sending final grades.");
	}
}
