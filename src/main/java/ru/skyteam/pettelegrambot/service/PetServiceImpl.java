package ru.skyteam.pettelegrambot.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skyteam.pettelegrambot.entity.Pet;
import ru.skyteam.pettelegrambot.repository.PetRepository;

import java.time.LocalDate;
import java.util.List;
@Service
public class PetServiceImpl implements PetService{
    private  ParentServiceImpl parentServiceImpl;
    private  PetRepository petRepository;

   public PetServiceImpl(ParentServiceImpl parentServiceImpl, PetRepository petRepository ) {
       this.parentServiceImpl = parentServiceImpl;
       this.petRepository = petRepository;
   }

    @Override
    public Pet create(Pet pet) {
        return petRepository.save(pet);
    }

    @Override
    public Pet read(long id) {
        return petRepository.getPetById(id);
    }

    @Override
    public Pet update(Pet pet) {
        return petRepository.save(pet);
    }

    @Override
    public ResponseEntity<Pet> delete(long id) {
        petRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public List<Pet> readAll() {
        return petRepository.findAll();
    }

    public List<Pet> findByParentId(Long parentId) {
        return petRepository.findAllByParentId(parentId);
    }

    public List<Pet> findAllByChatId(Long chatId) {
        return findByParentId(parentServiceImpl.findParentByChatId(chatId).getId());
    }


    public List<Pet> listPetForReport(){
        return petRepository.findAllByDateOfEndReportAfter(LocalDate.now());
    }

    public List<Pet> listPetForEndingReport() {
        return petRepository.findAllByDateOfEndReportEquals(LocalDate.now().minusDays(1));
    }


}
