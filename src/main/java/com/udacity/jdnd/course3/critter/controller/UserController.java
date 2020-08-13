package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.dto.CustomerDTO;
import com.udacity.jdnd.course3.critter.dto.EmployeeDTO;
import com.udacity.jdnd.course3.critter.dto.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer = convertCustomerDTOToEntity(customerDTO);
        customer = customerService.save(customer);
        return convertCustomerEntityToDTO(customer);
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<Customer> customers = customerService.findAllCustomers();
        return convertCustomerListToDTO(customers);
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        Customer customer = customerService.findOwnerByPet(petId);
        return convertCustomerEntityToDTO(customer);
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = convertEmployeeDTOToEntity(employeeDTO);
        employee = employeeService.save(employee);
        return convertEmployeeEntityToDTO(employee);
    }

    @GetMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Employee employee = employeeService.findEmployee(employeeId);
        return convertEmployeeEntityToDTO(employee);
    }

    @PostMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        employeeService.setEmployeeAvailability(daysAvailable, employeeId);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> employees = employeeService.findEmployeesForService(employeeDTO.getDate().getDayOfWeek(),
                employeeDTO.getSkills());
        return convertEmployeeListToDTO(employees);
    }

    /*
    * CUSTOMER UTILITIES
    */
    private Customer convertCustomerDTOToEntity(CustomerDTO customerDTO) {
        return new Customer(customerDTO.getName(), customerDTO.getPhoneNumber(), customerDTO.getNotes());
    }

    private CustomerDTO convertCustomerEntityToDTO(Customer customer) {
        if (customer.getPets() != null) {
            List<Long> petIds = customer.getPets().stream()
                    .map(pet -> pet.getId())
                    .collect(Collectors.toList());
            return new CustomerDTO(customer.getId(), customer.getName(), customer.getPhoneNumber(),
                    customer.getNotes(), petIds);
        }
        return new CustomerDTO(customer.getId(), customer.getName(), customer.getPhoneNumber(),
                customer.getNotes(), null);
    }

    private List<CustomerDTO> convertCustomerListToDTO(List<Customer> customers) {
        return customers.stream()
                .map(this::convertCustomerEntityToDTO)
                .collect(Collectors.toList());
    }

    /*
    * EMPLOYEE UTILITIES
    */
    private Employee convertEmployeeDTOToEntity(EmployeeDTO employeeDTO) {
        return new Employee(employeeDTO.getName(), employeeDTO.getDaysAvailable(), employeeDTO.getSkills());
    }

    private EmployeeDTO convertEmployeeEntityToDTO(Employee employee) {
        return new EmployeeDTO(employee.getId(), employee.getName(), employee.getDaysAvailable(),
                employee.getSkills());
    }

    private List<EmployeeDTO> convertEmployeeListToDTO(List<Employee> employees) {
        return employees.stream()
                .map(this::convertEmployeeEntityToDTO)
                .collect(Collectors.toList());
    }
}
