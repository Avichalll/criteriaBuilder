package com.example.criteriabuilder.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.criteriabuilder.Dao.EmployeSearchDoa;
import com.example.criteriabuilder.Dao.SearchRequest;
import com.example.criteriabuilder.Model.Employee;
import com.example.criteriabuilder.Model.EmployeeRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/Emp")
@RequiredArgsConstructor

public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final EmployeSearchDoa employeSearchDoa;

    @PostMapping("/savedata")
    public ResponseEntity<?> postMethodName(@Valid @RequestBody Employee employee) {

        employeeRepository.save(employee);
        return ResponseEntity.accepted().build();

    }

    @GetMapping("/search1")
    public ResponseEntity<List<Employee>> searchEmployee(
            @RequestParam(required = false) String firstname,
            @RequestParam(required = false) String lastname,
            @RequestParam(required = false) String email

    ) {
        List<Employee> employees = employeSearchDoa.findAllBySimpleQuery(firstname, lastname, email);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/searchEmployees")
    public List<Employee> searchEmployees(
            @RequestParam(required = false) String firstname,
            @RequestParam(required = false) String lastname,
            @RequestParam(required = false) String email) {

        SearchRequest request = new SearchRequest();
        request.setFirstname(firstname);
        request.setLastname(lastname);
        request.setEmail(email);

        return employeSearchDoa.findAllByCriteria(request);
    }

}
