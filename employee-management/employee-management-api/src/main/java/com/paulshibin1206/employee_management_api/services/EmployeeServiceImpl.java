package com.paulshibin1206.employee_management_api.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.paulshibin1206.employee_management_api.entity.EmployeeEntity;
import com.paulshibin1206.employee_management_api.model.Employee;
import com.paulshibin1206.employee_management_api.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private EmployeeRepository employeeRepository;

	public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	@Override
	public Employee createEmployee(Employee employee) {

		EmployeeEntity employeeEntity = new EmployeeEntity();
		BeanUtils.copyProperties(employee, employeeEntity);
		employeeRepository.save(employeeEntity);

		return employee;
	}

	@Override
	public List<Employee> getAllEmployees() {

		List<EmployeeEntity> employeeEntity = employeeRepository.findAll();

		List<Employee> employee = employeeEntity.stream()
				.map(emp -> new Employee(emp.getId(), emp.getFirstName(), emp.getLastName(), emp.getEmailId()))
				.collect(Collectors.toList());

		return employee;
	}

	@Override
	public boolean deleteEmployee(long id) {
		try {
			EmployeeEntity employeeEntity = employeeRepository.findById(id).get();
			employeeRepository.delete(employeeEntity);
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}

	}

	@Override
	public Employee getEmployeeById(long id) {
		EmployeeEntity employeeEntity = employeeRepository.findById(id).get();
		Employee employee = new Employee();
		BeanUtils.copyProperties(employeeEntity, employee);
		return employee;
	}

	@Override
	public Employee updateEmployee(long id, Employee employee) {
		
		EmployeeEntity employeeEntity = employeeRepository.findById(id).get();
		employeeEntity.setFirstName(employee.getFirstName());
		employeeEntity.setLastName(employee.getLastName());
		employeeEntity.setEmailId(employee.getEmailId());
		
		employeeRepository.save(employeeEntity);
		
		return employee;
	}



}
