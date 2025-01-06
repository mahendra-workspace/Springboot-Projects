package com.example.demo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Department;
import com.example.demo.entities.Employee;
import com.example.demo.entities.Users;
import com.example.demo.repositories.DepartmentRepository;
import com.example.demo.repositories.EmployeeRepository;
import com.example.demo.repositories.UsersRepository;

import jakarta.transaction.Transactional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private DepartmentRepository departmentRepository;
    
    
    @Transactional
    public Employee addEmployee(Employee employee) {

        Optional<Users> userOpt = usersRepository.findById(employee.getUser().getUserId());
        if (!userOpt.isPresent()) {
            throw new IllegalArgumentException("User not found with the provided userId");
        }
        employee.setUser(userOpt.get());


        Department department = employee.getDepartment();
        if (department != null) {

            Optional<Department> existingDepartment = departmentRepository.findById(department.getDeptNumber());
            if (existingDepartment.isPresent()) {
                employee.setDepartment(existingDepartment.get());
            } else {
            	
                departmentRepository.save(department);
                employee.setDepartment(department); 
            }
        }


        return employeeRepository.save(employee);
    }
    
    @Transactional
    public Employee updateEmployee(int empId, Employee employeeDetails) {

        Optional<Employee> existingEmployeeOpt = employeeRepository.findById(empId);
        if (!existingEmployeeOpt.isPresent()) {
            throw new IllegalArgumentException("Employee not found with the provided empId");
        }

        // Get the existing employee
        Employee existingEmployee = existingEmployeeOpt.get();

        // Update fields
        if (employeeDetails.getRole() != null) {
            existingEmployee.setRole(employeeDetails.getRole());
        }
        if (employeeDetails.getSalary() != 0) {
            existingEmployee.setSalary(employeeDetails.getSalary());
        }
        if (employeeDetails.getJoiningDate() != null) {
            existingEmployee.setJoiningDate(employeeDetails.getJoiningDate());
        }

        // Update user and department
        if (employeeDetails.getUser() != null) {
            Optional<Users> userOpt = usersRepository.findById(employeeDetails.getUser().getUserId());
            if (userOpt.isPresent()) {
                existingEmployee.setUser(userOpt.get());
            }
        }

        if (employeeDetails.getDepartment() != null) {
            Department department = employeeDetails.getDepartment();
            Optional<Department> existingDepartmentOpt = departmentRepository.findById(department.getDeptNumber());
            if (existingDepartmentOpt.isPresent()) {
                existingEmployee.setDepartment(existingDepartmentOpt.get());
            } else {
                departmentRepository.save(department);  // Save new department if it doesn't exist
                existingEmployee.setDepartment(department);
            }
        }

        // Save and return the updated employee
        return employeeRepository.save(existingEmployee);
    }
    

    public Employee getEmployeeDetailsById(int empId) {
        Optional<Employee> employee = Optional.ofNullable(employeeRepository.findEmployeeDetailsById(empId));
        return employee.orElseThrow(() -> new RuntimeException("Employee not found with id: " + empId));
    }
	
}
