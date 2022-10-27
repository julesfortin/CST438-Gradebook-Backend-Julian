package com.cst438.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentDTO;
import com.cst438.domain.EnrollmentRepository;

@RestController
public class EnrollmentController {

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	EnrollmentRepository enrollmentRepository;

	/*
	 * endpoint used by registration service to add an enrollment to an existing
	 * course.
	 */
	@PostMapping("/enrollment")
	@Transactional
	public EnrollmentDTO addEnrollment(@RequestBody EnrollmentDTO enrollmentDTO) {
		
		//TODO  complete this method in homework 4
		
		Enrollment e = new Enrollment();
		e.setStudentEmail(enrollmentDTO.studentEmail);
		e.setStudentName(enrollmentDTO.studentName);
		
		//we have to relate it to a course
		Course c = courseRepository.findById(enrollmentDTO.course_id).orElse(null);
		//then, we check the value of course object c, and throw an error if it doesn't exist
		if(c == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course id not found.");
		}
		
		//putting course value into enrollment object
		e.setCourse(c);
		e = enrollmentRepository.save(e);
		
		//grabbing the id to store into the DTO
		enrollmentDTO.id = e.getId();
		
		return enrollmentDTO;
		
//		this.studentEmail=studentEmail;
//		this.studentName=studentName;
//		this.course_id = course_id;		
	}

}
