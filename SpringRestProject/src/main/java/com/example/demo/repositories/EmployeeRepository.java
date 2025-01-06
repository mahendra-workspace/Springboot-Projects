package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Employee;
import com.example.demo.entities.Users;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer>{

    @Query("SELECT e FROM Employee e " +
           "JOIN FETCH e.user u " +
           "JOIN FETCH e.department d " +
           "WHERE e.empId = :empId")
    Employee findEmployeeDetailsById(@Param("empId") int empId);
    
    
    // Custom method to delete employees by user
    void deleteByUser(Users user);
	
}
