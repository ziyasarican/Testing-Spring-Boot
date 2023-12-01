package com.example.springboottesting.repository;


import com.example.springboottesting.model.Employee;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.OptionalAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest // only load @Repository tag with jpa. Only works in memory db.
public class EmployeeRepositoryTest {


    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;


    @BeforeEach
    public void setup(){
        employee = Employee.builder()
                .firstName("Ziya")
                .lastName("Sarican")
                .email("zs@gmail.com")
                .build();
    }


    // BDD style -> given, when, then
    @DisplayName("test of givenEmployeeObject_whenSave_thenReturnSavedEmployee")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){

        // savedEmployee fonksiyonuna bir employee gönderilecek. Gönderildikten sonra sonucu istenenle karşılaştır

        //given - precondition/setup


        // we dont need to build because of @BeforeEach
        //Employee employee = Employee.builder()
        //        .firstName("Ziya")
        //        .lastName("Sarican")
        //        .email("zs@gmail.com")
        //        .build();




        //when - action/behaviour
        Employee savedEmployee = employeeRepository.save(employee);

        //then - verify
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee).isEqualTo(employee);
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }



    @Test
    public void givenEmployeeList_whenFindAll_thenEmployeesList(){
        //bu defa employelist gönderiliyor, when kısmı, kaydediliyor ve ilgili fonksiyon çıktısıyla karşılaştırılıyor.

        //given
        Employee employee1 = Employee.builder()
                .firstName("Ziya")
                .lastName("Sarican")
                .email("zs@gmail.com")
                .build();
        Employee employee2 = Employee.builder()
                .firstName("Ziya2")
                .lastName("Sarican2")
                .email("zs2@gmail.com")
                .build();
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        //when
        List<Employee> employeeList = employeeRepository.findAll();

        //then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
        assertThat(employeeList.get(0)).isEqualTo(employee1);
        assertThat(employeeList.get(1)).isEqualTo(employee2);
    }

    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject(){
        //given
        //Employee employee = Employee.builder()
        //        .firstName("Ziya")
        //        .lastName("Sarican")
        //        .email("zs@gmail.com")
        //        .build();
        //employeeRepository.save(employee);

        //when
        Optional<Employee> employeeDB = employeeRepository.findById(employee.getId());

        //then
        assertThat(employeeDB).isNotNull();

    }

    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){
        //given
        Employee employee = Employee.builder()
                .firstName("Ziya")
                .lastName("Sarican")
                .email("zs@gmail.com")
                .build();
        employeeRepository.save(employee);

        //when
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setFirstName("Ziya2");
        savedEmployee.setLastName("Sarican2");
        savedEmployee.setEmail("zs@gmail.com");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        //then
        assertThat(updatedEmployee.getFirstName()).isEqualTo(savedEmployee.getFirstName());
        assertThat(updatedEmployee.getLastName()).isEqualTo(savedEmployee.getLastName());
        assertThat(updatedEmployee.getEmail()).isEqualTo(savedEmployee.getEmail());

    }

    @Test
    public void givenEmployeeObject_whenDeleteEmployee_thenRemoveEmployee(){
        //given
        //Employee employee = Employee.builder()
        //        .firstName("Ziya")
        //        .lastName("Sarican")
        //        .email("zs@gmail.com")
        //        .build();
        employeeRepository.save(employee);

        //when
        employeeRepository.delete(employee);
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

        //then
        assertThat(employeeOptional).isEmpty();
    }

    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLNamed_thenReturnEmployeeObject(){
        //given
        //Employee employee = Employee.builder()
        //        .firstName("Ziya")
        //        .lastName("Sarican")
        //        .email("zs@gmail.com")
        //        .build();
        employeeRepository.save(employee);
        String firstName = "Ziya";
        String lastName = "Sarican";

        //when
        Employee savedEmployee = employeeRepository.findByJPQLNamed(firstName, lastName);

        //then
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isEqualTo(employee.getId());
    }
}
