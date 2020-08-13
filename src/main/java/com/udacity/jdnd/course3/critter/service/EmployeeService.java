package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.type.EmployeeSkill;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    public Employee save(Employee employee) { return employeeRepository.save(employee); }

    public List<Employee> findAllEmployees() {
        return Lists.newArrayList(employeeRepository.findAll());
    }

    public Employee findEmployee(Long employeeId) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        return optionalEmployee.orElseThrow(EntityNotFoundException::new);
    }

    public void setEmployeeAvailability(Set<DayOfWeek> daysAvailable, Long employeeId) {
        Employee employee = findEmployee(employeeId);
        employee.setDaysAvailable(daysAvailable);
        employeeRepository.save(employee);
    }

    public List<Employee> findEmployeesForService(DayOfWeek day, Set<EmployeeSkill> skills) {
        List<Employee> allEmployees = findAllEmployees();
        List<Employee> employeesAvailable = new ArrayList<>();
        for (Employee employee : allEmployees) {
            if (employee.getDaysAvailable().contains(day) && employee.getSkills().containsAll(skills))
                employeesAvailable.add(employee);
        }
        return employeesAvailable;
    }
}
