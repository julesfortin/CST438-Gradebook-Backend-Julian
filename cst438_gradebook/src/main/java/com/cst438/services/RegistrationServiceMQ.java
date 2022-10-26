package com.cst438.services;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Course;
import com.cst438.domain.CourseDTOG;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentDTO;
import com.cst438.domain.EnrollmentRepository;


public class RegistrationServiceMQ extends RegistrationService {

	@Autowired
	EnrollmentRepository enrollmentRepository;

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public RegistrationServiceMQ() {
		System.out.println("MQ registration service ");
	}

	// ----- configuration of message queues

	@Autowired
	Queue registrationQueue;


	// ----- end of configuration of message queue

	// receiver of messages from Registration service
	
	@RabbitListener(queues = "gradebook-queue")
	@Transactional
	public void receive(EnrollmentDTO enrollmentDTO) {
		
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
		
		
		//sending attempt as message
		System.out.println("Sending rabbitmq message: " + e);
		rabbitTemplate.convertAndSend(registrationQueue.getName(), e);
		System.out.println("Message sent.");
	}

	// sender of messages to Registration Service
	@Override
	public void sendFinalGrades(int course_id, CourseDTOG courseDTO) {
		 
		//TODO  complete this method in homework 4
		//printing out a message for the console
		System.out.println("Sending final grades" + course_id + " " + courseDTO);
		
		//put call to registration URL. appending parameters to end of localhost url
		//restTemplate.put(registration_url + "/course/" + course_id, courseDTO);
		rabbitTemplate.convertAndSend(registrationQueue.getName(), courseDTO);
		
		//another console message for debugging
		System.out.println("Message send to gradbook service for student " + course_id + " " + courseDTO);
		
	}

}
