package ru.skyteam.pettelegrambot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skyteam.pettelegrambot.entity.PetType;
import ru.skyteam.pettelegrambot.entity.Shelter;
import ru.skyteam.pettelegrambot.entity.Volunteer;
import ru.skyteam.pettelegrambot.repository.VolunteerRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class VolunteerServiceTest {

    @Mock
    private VolunteerRepository volunteerRepository;

    @InjectMocks
    private VolunteerServiceImpl volunteerServiceImpl;

    Shelter shelterCat = new Shelter(1l,PetType.CAT);
    Shelter shelterDog = new Shelter(2l,PetType.DOG);

    @Test
    public void createTest() {
        Volunteer volunteer1 = new Volunteer(1L, 1L, "Иванова Ксения", shelterCat);
        Mockito.when(volunteerRepository.save(volunteer1)).thenReturn(volunteer1);
        assertThat(volunteer1).isEqualTo(volunteerServiceImpl.create(volunteer1));
        assertThat(volunteerServiceImpl.create(volunteer1)).isNotNull();
    }

    @Test
    public void readTest() {
        Volunteer volunteer1 = new Volunteer(1L, 1L, "Иванова Ксения", shelterCat);
        Mockito.when(volunteerRepository.getVolunteerById(1L)).thenReturn(volunteer1);
        assertThat(volunteer1).isEqualTo(volunteerServiceImpl.read(1L));
        assertThat(volunteerServiceImpl.read(1L)).isNotNull();
    }

    @Test
    public void updateTest() {
        Volunteer volunteer1 = new Volunteer(1L, 1L, "Петрова Алина", shelterDog);
        Mockito.when(volunteerRepository.save(volunteer1)).thenReturn(volunteer1);
        assertThat(volunteer1).isEqualTo(volunteerServiceImpl.update(volunteer1));
        assertThat(volunteerServiceImpl.update(volunteer1)).isNotNull();
    }

    @Test
    public void deleteTest() {
        volunteerServiceImpl.delete(1L);
        verify(volunteerRepository).deleteById(1L);
    }

    @Test
    public void getAllTest() {
        List<Volunteer> arr = new ArrayList<Volunteer>(3);
        Volunteer volunteer1 = new Volunteer(1L, 1L, "Колесова Евгения", shelterCat);
        Volunteer volunteer2 = new Volunteer(2L, 2L, "Загородний Иван", shelterDog);
        Volunteer volunteer3 = new Volunteer(3L, 3L, "Левченко Петр", shelterDog);
        arr.add(volunteer1);
        arr.add(volunteer2);
        arr.add(volunteer3);
        Mockito.when(volunteerRepository.getAll()).thenReturn(arr);
        assertThat(arr).isEqualTo(volunteerServiceImpl.getAll());
        assertThat(volunteerServiceImpl.getAll()).isNotNull();
    }
}