package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private CustomerService customerService;

    public Pet save(Pet pet, Long ownerId) {
        customerService.addPetToOwner(pet, ownerId);
        return petRepository.save(pet);
    }

    public Pet findPet(Long petId) {
        Optional<Pet> optionalPet = petRepository.findById(petId);
        return optionalPet.orElseThrow(EntityNotFoundException::new);
    }

    public List<Pet> findAllPets() {
        return Lists.newArrayList(petRepository.findAll());
    }

    public List<Pet> findPetsByOwner(Long ownerId) {
        Customer owner = customerService.findCustomer(ownerId);
        return owner.getPets();
    }

}
