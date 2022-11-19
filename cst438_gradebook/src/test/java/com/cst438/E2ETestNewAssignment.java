package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;

import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentRepository;

public class E2ETestNewAssignment {
	
	public static final String CHROME_DRIVER_FILE_LOCATION = "/Users/vincentfortin/chromedriver";
	public static final String URL = "http://localhost:3000";
	public static final String ALIAS_NAME = "test";
	public static final int SLEEP_DURATION = 1000; // 1 second.
	
	public static final String TEST_ASSIGNMENT_NAME = "End to End Assignment 1";
	public static final String TEST_DUE_DATE = "2022-11-01";
	public static final String TEST_COURSE_ID = "40443";
	
	@Autowired
	AssignmentRepository assignmentRepository;
	
	@Test
	public void addAssignmentTest() throws Exception{
		// set the driver location and start driver
		//@formatter:off
		//
		// browser	property name 				Java Driver Class
		// -------  ------------------------    ----------------------
		// Edge 	webdriver.edge.driver 		EdgeDriver
		// FireFox 	webdriver.firefox.driver 	FirefoxDriver
		// IE 		webdriver.ie.driver 		InternetExplorerDriver
		// Chrome   webdriver.chrome.driver     ChromeDriver
		//
		//@formatter:on
	 
			//TODO update the property name for your browser 
		System.setProperty("webdriver.chrome.driver",
	                 CHROME_DRIVER_FILE_LOCATION);
		//TODO update the class ChromeDriver()  for your browser
		WebDriver driver = new ChromeDriver();
		
		// Puts an Implicit wait for 10 seconds before throwing exception
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		try {
			
			WebElement we;
			
			//grabbing the URL of the chrome driver 
			driver.get(URL);
			
			//We then need to create a wait time to let the browser download the page
			Thread.sleep(SLEEP_DURATION);
			
			//finding the "new assignment button on the page and clicking it
			we = driver.findElement(By.name("newAdd"));
			we.click();
			
			//then, we add the inputs for each text field needed: course id, assignment name, and due date
			we = driver.findElement(By.name("courseId"));
			we.sendKeys(TEST_COURSE_ID);
			
			we = driver.findElement(By.name("assignmentName"));
			we.sendKeys(TEST_ASSIGNMENT_NAME);
			
			we = driver.findElement(By.name("dueDate"));
			we.sendKeys(TEST_DUE_DATE);
			
			//then we click the add(submit) button
			we = driver.findElement(By.id("Add"));
			we.click();
			
			//perform another sleep
			Thread.sleep(SLEEP_DURATION);
			
			//checking to see if the assignment was added to the DB (hopefully the sqlDB generated 13??)
			Assignment a = assignmentRepository.findById(13).orElse(null);
			
			assertEquals(TEST_ASSIGNMENT_NAME, a.getName());
			
		} catch (Exception e) {
			
			throw e;
			
		} finally {
			
//			//we need to check if the test data already exists in the db
////			Enrollment x = null;
////	        do {
////	            x = enrollmentRepository.findByEmailAndCourseId(TEST_USER_EMAIL, TEST_COURSE_ID);
////	            if (x != null)
////	                enrollmentRepository.delete(x);
////	        } while (x != null);
//			Assignment a = null;
//			
//			do {
//				a = assignmentRepository.findAllById(null)
//			}
			
			//we need to delete the test data if it exists in the db
			Assignment a = assignmentRepository.findById(13).orElse(null);
			
			if(a != null) {
				assignmentRepository.delete(a);
			}
			
			driver.quit();
			
		}
		
				
	}
	

}
