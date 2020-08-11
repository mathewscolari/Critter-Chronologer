package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.dto.PetDTO;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    private PetService petService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet = convertPetDTOToEntity(petDTO);
        pet = petService.save(pet);
        return convertPetEntityToDTO(pet);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet = petService.findPet(petId);
        return convertPetEntityToDTO(pet);
    }

    @GetMapping
    public List<PetDTO> getPets(){
        List<Pet> pets = petService.findAllPets();
        return convertPetListToDTO(pets);
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> pets = petService.findPetsByOwner(ownerId);
        return convertPetListToDTO(pets);
    }

    // converts a Pet DTO to a Pet entity
    private Pet convertPetDTOToEntity(PetDTO petDTO) {
        return new Pet(petDTO.getType(), petDTO.getName(), petDTO.getBirthDate(), petDTO.getNotes());
    }

    // converts a Pet entity to a Pet DTO
    private PetDTO convertPetEntityToDTO(Pet pet) {
        return new PetDTO(pet.getId(), pet.getType(), pet.getName(), pet.getOwner().getId(),
                pet.getBirthDate(), pet.getNotes());
    }

    // converts a list of Pet entities to DTO objects
    private List<PetDTO> convertPetListToDTO(List<Pet> pets) {
        return pets.stream()
                .map(this::convertPetEntityToDTO)
                .collect(Collectors.toList());
    }
}
