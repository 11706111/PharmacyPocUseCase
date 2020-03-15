package org.pharmacy.poc.service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.json.JSONObject;
import org.pharmacy.poc.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Service
public class CompanyService {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	DiscoveryClient discoveryClient;
	@Autowired
	HttpHeaders httpHeaders;

	public CompanyService() {
		// Default Constructor
	}

	/* Gets all the student entities saved so far from H2 database */
	public Employee[] listEmployee(String columns, String records, String pageNo) {
		ServiceInstance employeeService = discoveryClient.getInstances("employee-service").get(0);
		String url = "http://" + employeeService.getHost() + ":" + employeeService.getPort() + "/" + "user/list";
		if (!StringUtils.isEmpty(columns) || !StringUtils.isEmpty(records) || !StringUtils.isEmpty(pageNo)) {
			url += "?";
		}
		if (!StringUtils.isEmpty(columns)) {
			url += "columns=" + columns + "&";
		}
		if (!StringUtils.isEmpty(records)) {
			url += "number=" + records + "&";
		}
		if (!StringUtils.isEmpty(pageNo)) {
			url += "page=" + pageNo + "&";
		}
		if (!StringUtils.isEmpty(columns) || !StringUtils.isEmpty(records) || !StringUtils.isEmpty(pageNo)) {
			url = url.substring(0, url.length() - 1);
		}
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		return restTemplate.getForObject(url, Employee[].class);
	}

	public List<Employee> save(List<Employee> employees) {
		List<Employee> employeesAdded = new ArrayList<>();
		ServiceInstance employeeService = discoveryClient.getInstances("employee-service").get(0);
		String url = "http://" + employeeService.getHost() + ":" + employeeService.getPort() + "/" + "user/save";
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		if (null != employees && !employees.isEmpty()) {
			for (Employee employee : employees) {
				JSONObject jsonObj = new JSONObject(employee);
				HttpEntity<String> request = new HttpEntity<>(jsonObj.toString(), httpHeaders);
				employeesAdded.add(restTemplate.postForObject(url, request, Employee.class));
			}
		}
		return employeesAdded;
	}

	public List<Employee> update(@Valid List<Employee> employees) {
		ServiceInstance employeeService = discoveryClient.getInstances("employee-service").get(0);
		String url = "http://" + employeeService.getHost() + ":" + employeeService.getPort() + "/" + "user/save";
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		if (null != employees && !employees.isEmpty()) {
			for (Employee employee : employees) {
				JSONObject jsonObj = new JSONObject(employee);
				HttpEntity<String> request = new HttpEntity<>(jsonObj.toString(), httpHeaders);
				restTemplate.put(url, request);
			}
		}
		return employees;
	}

}
