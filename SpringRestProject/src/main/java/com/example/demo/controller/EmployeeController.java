package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Department;
import com.example.demo.entities.Employee;
import com.example.demo.entities.Users;
import com.example.demo.repositories.DepartmentRepository;
import com.example.demo.repositories.UsersRepository;
import com.example.demo.services.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @GetMapping("/{empId}")
    public ResponseEntity<?> getEmployeeDetails(@PathVariable int empId) {
        try {
            Employee employee = employeeService.getEmployeeDetailsById(empId);
            return ResponseEntity.ok(employee);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Employee not found with id: " + empId);
        }
    }
	
    
    @PostMapping("/{userId}")
    public ResponseEntity<?> addEmployee(@PathVariable int userId, @RequestBody Employee employee) {

        Users user = usersRepository.findById(userId).orElse(null);
        
        if (user == null) {
            return ResponseEntity.status(404).body("user not found with id: " + userId); 
        }


        employee.setUser(user);

        Department department = employee.getDepartment();
        if (department != null) {
            Department existingDepartment = departmentRepository.findById(department.getDeptNumber()).orElse(null);
            if (existingDepartment == null) {

                departmentRepository.save(department);
            } else {
                employee.setDepartment(existingDepartment); 
            }
        }

        Employee savedEmployee = employeeService.addEmployee(employee);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }
    
    @PutMapping("/{empId}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable int empId, @RequestBody Employee employeeDetails) {
        try {
            Employee updatedEmployee = employeeService.updateEmployee(empId, employeeDetails);
            return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
