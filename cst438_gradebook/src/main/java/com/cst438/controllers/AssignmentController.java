package com.cst438.controllers;

import java.sql.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentListDTO.AssignmentDTO;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;

@RestController
@CrossOrigin(origins= {"http://localhost:3000"})
public class AssignmentController {
	
	//connecting the assignment repository
	@Autowired
	AssignmentRepository assignmentRepository;
	
	@Autowired
	CourseRepository courseRepository;
	
	@PostMapping("/assignment")
	@Transactional
	public AssignmentDTO newAssignment(@RequestBody AssignmentDTO dto) {
		String userEmail = "dwisneski@csumb.edu";
		// validate course and that the course instructor is the user
		Course c = courseRepository.findById(dto.courseId).orElse(null);
		if (c != null && c.getInstructor().equals(userEmail)) {
			// create and save new assignment
			// update and return dto with new assignment primary key
			Assignment a = new Assignment();
			a.setCourse(c);
			a.setName(dto.assignmentName);
			a.setDueDate(Date.valueOf(dto.dueDate));
			a.setNeedsGrading(1);
			a = assignmentRepository.save(a);
			dto.assignmentId=a.getId();
			return dto;
			
		} else {
			// invalid course
			throw new ResponseStatusException( 
                           HttpStatus.BAD_REQUEST, 
                          "Invalid course id.");
		}
	}

	
	@PostMapping("/assignment/new")
	public AssignmentDTO addAssignment(@RequestBody AssignmentDTO dto) {
		
		//creating a temp Assignment object a so that we can set its values, then add it in later
		Assignment a = new Assignment();
		a.setName(dto.assignmentName);
		a.setDueDate(Date.valueOf(dto.dueDate));
		
		//creating Course object and doing the same as previous lines. Grabbing course ID from the repository 
			//so that we can set it to the current Assignment object a
		Course c = courseRepository.findById(dto.courseId).orElse(null);

		
		if (c == null) {
			//throwing an exception, courseId was entered incorrectly
			throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Invalid course key." +dto.courseId);
		}
		
		a.setCourse(c);
		
		//saving object a to the repository, also getting primary key from a so that addAssignment function 
			//will return it when called
		a = assignmentRepository.save(a);
		dto.assignmentId = a.getId();
		
		return dto;
				
	}
	
	@PutMapping ("/assignment/")
	public void updateAssignment(@RequestBody AssignmentDTO dto) {
		
		//creating an Assignment object a and setting it to the returned assignment value when looking up by its id
		Assignment a = assignmentRepository.findById(dto.assignmentId).orElse(null);
		
		if(a == null) {
			//throwing an exception, assignmentId was entered incorrectly
			throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Invalid assignment key." +dto.assignmentId);
		}
		
		//setting values
		a.setName(dto.assignmentName);
		
		//we need to convert value of users input to the date object the function is expecting
		a.setDueDate(Date.valueOf(dto.dueDate));
		
		//saving Assignment object to repository
		assignmentRepository.save(a);
		
		//no return for void function
		
	}
	
	@DeleteMapping("/assignment/{id}")
	public void deleteAssignment(@PathVariable("id") int id) {
		
		//creating Assignment object a so that we can search by the given id
		Assignment a = assignmentRepository.findById(id).orElse(null);
		
		if(a == null) {
			//throwing an exception, primary key was entered incorrectly
			throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Invalid primary key. "+id);
		}
		
		//check if there are no grades for the assignment before doing the delete? ***
		
		//using repository function to delete assignment object
		assignmentRepository.delete(a);
		
		//no return for void function
		
	}

}
