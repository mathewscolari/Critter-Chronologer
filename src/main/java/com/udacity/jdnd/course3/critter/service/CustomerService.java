package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PetService petService;

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer findCustomer(Long custId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(custId);
        return optionalCustomer.orElseThrow(EntityNotFoundException::new);
    }

    public List<Customer> findAllCustomers() { return Lists.newArrayList(customerRepository.findAll()); }

    public Customer findOwnerByPet(Long petId) {
        Pet pet = petService.findPet(petId);
        return pet.getOwner();
    }

    public Customer addPetToOwner(Pet pet, Long ownerId) {
        Customer owner = findCustomer(ownerId);
        pet.setOwner(owner);

        List<Pet> pets = owner.getPets() != null ? owner.getPets() : new ArrayList<>();
        pets.add(pet);
//        owner.setPets(pets);

//        save(owner);
        return owner;
    }
}