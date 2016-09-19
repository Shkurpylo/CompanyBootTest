package com.manager.controller;


import com.manager.entity.Company;
import com.manager.service.ICompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;


@Controller
@RequestMapping("/")
public class CompanyController {

	@Autowired
	private ICompanyService companyService;

	@RequestMapping("/")
	public String home() {
 		return "home";
 	}

	@RequestMapping(value="/company/{id}", method = RequestMethod.GET )
	public ResponseEntity<Company> getCompanyById(@PathVariable("id") Integer id) {
		Company company = companyService.getCompanyById(id);
		return new ResponseEntity<Company>(company, HttpStatus.OK);
	}

	@RequestMapping(value= "/company", method = RequestMethod.GET)
	public ResponseEntity<List<Company>> getAllCompanies() {
		List<Company> list = companyService.getAllCompanies();
		return new ResponseEntity<List<Company>>(list, HttpStatus.OK);
	}

	@RequestMapping(value= "/company", method = RequestMethod.POST)
	public ResponseEntity<Void> addCompany(@RequestBody Company company, UriComponentsBuilder builder) {
		boolean flag = companyService.addCompany(company);
		if (flag) {
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		} else {
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(builder.path("/company/{id}").buildAndExpand(company.getId()).toUri());
			return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
		}
	}

	@RequestMapping(value="/company/{id}", method = RequestMethod.PUT )
	public ResponseEntity<Company> updateCompany(@RequestBody Company company) {
		companyService.updateCompany(company);
		return new ResponseEntity<Company>(company, HttpStatus.OK);
	}


	@RequestMapping(value="/company/{id}", method = RequestMethod.DELETE )
	public ResponseEntity<Void> deleteCompany(@PathVariable("id") Integer id) {
		companyService.deleteCompany(id);

		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}	
} 