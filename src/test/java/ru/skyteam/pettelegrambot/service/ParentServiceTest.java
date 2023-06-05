package ru.skyteam.pettelegrambot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skyteam.pettelegrambot.entity.Parent;
import ru.skyteam.pettelegrambot.entity.Pet;
import ru.skyteam.pettelegrambot.entity.PetType;
import ru.skyteam.pettelegrambot.entity.Shelter;
import ru.skyteam.pettelegrambot.repository.ParentRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class ParentServiceTest {

    @Mock
    private ParentRepository parentRepository;
    @InjectMocks
    private ParentServiceImpl parentServiceImpl;

    Shelter shelterCat = new Shelter(1l,PetType.CAT);
    Shelter shelterDog = new Shelter(2l,PetType.DOG);
    List<Pet> pets = new ArrayList<>();
    List<Pet> pets2  = new ArrayList<>();


    @Test
    public void createTest() {
        List<Pet> pets = new ArrayList<Pet>(2);
        Pet pet1 = new Pet(12L,PetType.CAT, "Мурзик", shelterCat);
        Pet pet2 = new Pet(28L, PetType.DOG, "Шарик", shelterDog);
        pets.add(pet1);
        pets.add(pet2);
        Parent parent1 = new Parent(1L, 325L, "Грибоедова Анастасия", "8-888-8888", pets);
        Mockito.when(parentRepository.save(parent1)).thenReturn(parent1);
        assertThat(parent1).isEqualTo(parentServiceImpl.create(parent1));
        assertThat(parentServiceImpl.create(parent1)).isNotNull();
    }

    @Test
    public void readTest() {
        List<Pet> pets2 = new ArrayList<Pet>(1);
        Pet pet1 = new Pet(44L, PetType.DOG, "Джек", shelterDog);
        pets2.add(pet1);
        Parent parent1 = new Parent(2L, 2365L, "Иванов Фёдор", "9-999-9999", pets2);
        Mockito.when(parentRepository.getParentById(1L)).thenReturn(parent1);
        assertThat(parent1).isEqualTo(parentServiceImpl.read(1L));
        assertThat(parentServiceImpl.read(1L)).isNotNull();
    }

    @Test
    public void updateTest() {
        List<Pet> pets2 = new ArrayList<Pet>(1);
        Pet pet1 = new Pet(44L, PetType.DOG, "Джек", shelterDog);
        pets2.add(pet1);
        Parent parent1 = new Parent(2L, 2365L, "Иванов Фёдор", "7-777-7777", pets2);
        Mockito.when(parentRepository.save(parent1)).thenReturn(parent1);
        assertThat(parent1).isEqualTo(parentServiceImpl.update(parent1));
        assertThat(parentServiceImpl.update(parent1)).isNotNull();
    }

    @Test
    public void deleteTest() {
        parentServiceImpl.delete(1L);
        verify(parentRepository).deleteById(1L);
    }

    @Test
    public void readAllTest() {
        List<Parent> parents = new ArrayList<Parent>(2);
        Parent parent1 = new Parent(1L, 325L, "Грибоедова Анастасия", "8-888-8888", pets);
        Parent parent2 = new Parent(2L, 2365L, "Иванов Фёдор", "7-777-7777", pets2);
        parents.add(parent1);
        parents.add(parent2);
        Mockito.when(parentRepository.findAll()).thenReturn(parents);
        assertThat(parents).isEqualTo(parentServiceImpl.readAll());
        assertThat(parentServiceImpl.readAll()).isNotNull();
    }
    @Test
    public void testFindParentByChatId() {
        Long chatId = 12349L;
        Parent parent = new Parent();
        parent.setId(1L);
        parent.setChatId(chatId);
        Mockito.when(parentRepository.getParentByChatId(chatId)).thenReturn(parent);
        Parent result = parentServiceImpl.findParentByChatId(chatId);
        assertEquals(parent, result);
    }

    @Test
    public void testSave() {
        Parent parent = new Parent();
        parent.setFullName("Антон Денисов");
        parent.setPhoneNumber("+76543212255");
        Mockito.when(parentRepository.save(parent)).thenReturn(parent);
        Parent savedParent = parentServiceImpl.save(parent);
        Mockito.verify(parentRepository, Mockito.times(1)).save(parent);
        assertEquals(parent, savedParent);
    }
}
