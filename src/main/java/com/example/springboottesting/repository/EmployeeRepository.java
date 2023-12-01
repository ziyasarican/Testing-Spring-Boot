package com.example.springboottesting.repository;

import com.example.springboottesting.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface EmployeeRepository  extends JpaRepository<Employee, Long> {


    //Define custom query using JPQL with named params
    @Query("select e from Employee e where e.firstName =:firstName and e.lastName =:lastName")
    Employee findByJPQLNamed(@Param("firstName") String firstName, @Param("lastName") String lastName);

    Optional<Employee> findByEmail(String email);
}
