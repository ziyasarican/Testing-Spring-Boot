package com.example.springboottesting.service;

import com.example.springboottesting.exception.ResourceNotFoundException;
import com.example.springboottesting.model.Employee;
import com.example.springboottesting.repository.EmployeeRepository;
import com.example.springboottesting.service.impl.EmployeeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup(){

        employee = Employee.builder()
            .firstName("ziya")
            .lastName("sarican")
            .email("zs@test.com")
            .build();
    }

    //saveEmployee method
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject(){
        //given

        /*
        Biz service içindeki saveEmployee metodunu test etmek istiyoruz. Bunun için repository ile findByEmail'e bakmamız
        ve girilen employe'deki emaillerin eşleşmemesi lazım.
        Yani BDDMockito ile bir metodun senaryosu yazılı ve sonu olarak istenen durum return edilir.
         */
        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());

        /*
        Şimdi ise service sınıfının save metoduna yapılan çağrı simüle edilmeli.

         */
        BDDMockito.given(employeeService.saveEmployee(employee)).willReturn(employee);

        //when
        Employee savedEmployee = employeeService.saveEmployee(employee);

        //then
        Assertions.assertThat(savedEmployee).isNotNull();
    }

    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowResourceNotFoundException(){
        //given

        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        //when
        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        //save metodunun asla çağrılmadığını kontrol ediyoruz
        //then
        Mockito.verify(employeeRepository, Mockito.never()).save(any(Employee.class));


    }

    @Test
    public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList(){
        //given
        Employee employee1 = employee = Employee.builder()
                .firstName("ziya2")
                .lastName("sarican2")
                .email("zs2@test.com")
                .build();
        BDDMockito.given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));


        //when
        List<Employee> employeeList = employeeService.getAllEmployees();

        //then
        Assertions.assertThat(employeeList).isNotNull();
        Assertions.assertThat(employeeList).size().isEqualTo(2);
        Assertions.assertThat(employeeList.get(0).getEmail()).isEqualTo(employee.getEmail());
    }


    //negative scenario
    @Test
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeesList(){
        //given
        Employee employee1 = employee = Employee.builder()
                .firstName("ziya2")
                .lastName("sarican2")
                .email("zs2@test.com")
                .build();
        //findall çağrıldığında emptylist dönsün
        BDDMockito.given(employeeRepository.findAll()).willReturn(Collections.emptyList());


        //when
        List<Employee> employeeList = employeeService.getAllEmployees();

        //then
        Assertions.assertThat(employeeList).isEmpty();
        Assertions.assertThat(employeeList).size().isEqualTo(0);
    }

    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject(){
        //given
        BDDMockito.given(employeeRepository.findById(0L)).willReturn(Optional.of(employee));


        //when
        Employee savedEmployee = employeeService.getEmployeeById(employee.getId()).get();

        //then
        Assertions.assertThat(savedEmployee).isNotNull();
        Assertions.assertThat(savedEmployee).isEqualTo(employee);
    }


}
