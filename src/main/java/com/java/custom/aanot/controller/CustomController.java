package com.java.custom.aanot.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.java.custom.aanot.bean.Person;
import com.java.custom.aanot.cust.PostJson;

@RestController
public class CustomController {
	
	@PostJson(path="/register")
	public Person createPerson(@RequestBody Person person) {
		Person tempP = new Person();
		System.out.println("Person Id " + person.getId());
		System.out.println("Person Name " + person.getName());
		tempP.setId(person.getId());
		tempP.setName(person.getName());
		return tempP;
	}
}
