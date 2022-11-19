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
 
	@Autowired
	Queue registrationQueue;
 
	public RegistrationServiceMQ() {
		System.out.println("MQ registration service ");
	}
 
	// receiver of messages from Registration service
	@RabbitListener(queues = "gradebook-queue")
	public void receive(EnrollmentDTO enrollmentDTO) {
		System.out.println("Receive enrollment :" + enrollmentDTO);
		Course c = courseRepository.findById(enrollmentDTO.course_id).orElse(null);
		if (c != null) {
			Enrollment e = new Enrollment();
			e.setCourse(c);
			e.setStudentEmail(enrollmentDTO.studentEmail);
			e.setStudentName(enrollmentDTO.studentName);
			enrollmentRepository.save(e);
			System.out.println("Success");
		} else {
			System.out.println("Fail");
		}
	}
 
	// sender of messages to Registration Service
	@Override
	public void sendFinalGrades(int course_id, CourseDTOG courseDTO) {
		System.out.println("Sending final grades rabbitmq: " + course_id);
		rabbitTemplate.convertAndSend(registrationQueue.getName(), courseDTO);		
	}
}
