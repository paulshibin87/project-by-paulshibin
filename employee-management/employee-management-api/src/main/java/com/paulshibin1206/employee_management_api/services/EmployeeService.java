package com.paulshibin1206.employee_management_api.services;

import java.util.List;

import com.paulshibin1206.employee_management_api.model.Employee;


public interface EmployeeService {

	Employee createEmployee(Employee employee);

	List<Employee> getAllEmployees();

	boolean deleteEmployee(long id);

	Employee getEmployeeById(long id);

	Employee updateEmployee(long id, Employee employee);

}
