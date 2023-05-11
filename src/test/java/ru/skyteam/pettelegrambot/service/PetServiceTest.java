package ru.skyteam.pettelegrambot.service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skyteam.pettelegrambot.entity.Pet;
import ru.skyteam.pettelegrambot.entity.PetType;
import ru.skyteam.pettelegrambot.entity.Shelter;
import ru.skyteam.pettelegrambot.repository.PetRepository;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetServiceImpl petServiceImpl;

    Shelter shelterCat = new Shelter(1l,PetType.CAT);
    Shelter shelterDog = new Shelter(2l,PetType.DOG);


    @Test
    public void createTest() {
        Pet pet1 = new Pet(12L,PetType.CAT, "Мурзик", shelterCat);
        when(petRepository.save(pet1)).thenReturn(pet1);
        assertThat(pet1).isEqualTo(petServiceImpl.create(pet1));
        assertThat(petServiceImpl.create(pet1)).isNotNull();
    }

    @Test
    public void readTest() {
        Pet pet1 = new Pet(12L, PetType.CAT, "Мурзик", shelterCat);
        when(petRepository.getPetById(1L)).thenReturn(pet1);
        assertThat(pet1).isEqualTo(petServiceImpl.read(1L));
        assertThat(petServiceImpl.read(1L)).isNotNull();
    }

    @Test
    public void updateTest() {
        Pet pet2 = new Pet(28L, PetType.DOG, "Шарик", shelterDog);
        when(petRepository.save(pet2)).thenReturn(pet2);
        assertThat(pet2).isEqualTo(petServiceImpl.update(pet2));
        assertThat(petServiceImpl.update(pet2)).isNotNull();
    }

    @Test
    public void deleteTest() {
        petServiceImpl.delete(28L);
        verify(petRepository).deleteById(28L);
    }

    @Test
    public void getAllTest() {
        List<Pet> arr = new ArrayList<Pet>(3);
        Pet pet1 = new Pet(12L, PetType.CAT, "Мурзик", shelterCat);
        Pet pet2 = new Pet(44L, PetType.DOG, "Джек", shelterDog);
        Pet pet3 = new Pet(32L, PetType.CAT, "Багира", shelterCat);
        arr.add(pet1);
        arr.add(pet2);
        arr.add(pet3);
        when(petRepository.findAll()).thenReturn(arr);
        assertThat(arr).isEqualTo(petServiceImpl.readAll());
        assertThat(petServiceImpl.readAll()).isNotNull();
    }
}
