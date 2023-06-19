package ru.skyteam.pettelegrambot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.skyteam.pettelegrambot.entity.*;
import ru.skyteam.pettelegrambot.repository.PetRepository;
import ru.skyteam.pettelegrambot.repository.VolunteerRepository;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)


public class ShelterTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    PetRepository petRepository;
    @Autowired
    VolunteerRepository volunteerRepository;

    @Test
    void givenNoSheltersInDatabase_whenGetShelters_thenEmptyJsonArray() throws Exception {
        mockMvc.perform(get("/shelter/getListShelters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void givenNoSheltersInDatabase_whenShelterAddedItIsAddIsCorrect() throws Exception {
        Pet testPet = new Pet(1L, PetType.CAT, "Мурзик", null);
        List<Pet> pets = new ArrayList<Pet>();
        pets.add(testPet);
        petRepository.saveAll(pets);
        Volunteer testVolunteer = new Volunteer(1L, 0L, "Евсеева Елена");
        List<Volunteer> volunteers = new ArrayList<Volunteer>();
        volunteers.add(testVolunteer);
        volunteerRepository.saveAll(volunteers);

       Shelter testShelter = new Shelter(1L, PetType.CAT, volunteers, pets);

        mockMvc.perform(post("/shelter/create")
                        .content(objectMapper.writeValueAsString(testShelter))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.shelterType").value("CAT"))
                .andExpect(jsonPath("$.pets[*].name").value(hasItem("Мурзик")))
                .andExpect(jsonPath("$.volunteers[*].fullName").value(hasItem("Евсеева Елена")));

    }

    @Test
    void givenSheltersInDatabase_thenShelterFoundById() throws Exception {

        Shelter testShelter = new Shelter(1L, PetType.CAT);

        mockMvc.perform(post("/shelter/create")
                        .content(objectMapper.writeValueAsString(testShelter))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.shelterType").value("CAT"));

        mockMvc.perform(get("/shelter/read/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.shelterType").value("CAT"));;
    }

    @Test
    void givenShelterInDatabase_whenShelterIsEdited_thenItIsChangedInDatabase() throws Exception {

        Shelter testShelter = new Shelter(1L, PetType.CAT);

        mockMvc.perform(post("/shelter/create")
                        .content(objectMapper.writeValueAsString(testShelter))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.shelterType").value("CAT"));

        Shelter updatedShelter = new Shelter(testShelter.getId(), testShelter.getShelterType());
               long id = updatedShelter.getId();

        mockMvc.perform(put("/shelter/update/{id}", id)
                        .content(objectMapper.writeValueAsString(updatedShelter))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.shelterType").value("CAT"));

        mockMvc.perform(get("/shelter/read/{id}", id))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.shelterType").value("CAT"));
    }

    @Test
    void givenSheltersInDatabase_thenShelterDeletedByIdIsCorrect() throws Exception {

        Shelter testShelter = new Shelter(1L, PetType.CAT);

        mockMvc.perform(post("/shelter/create")
                        .content(objectMapper.writeValueAsString(testShelter))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.shelterType").value("CAT"));

        long id = testShelter.getId();

        mockMvc.perform(delete("/shelter/delete/{id}", id))
                .andExpect(status().isOk());

        mockMvc.perform(get("/shelter"))
                .andExpect(status().isNotFound());
    }
}
