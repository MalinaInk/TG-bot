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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)


public class PetTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    void givenNoPetsInDatabase_whenGetPets_thenEmptyJsonArray() throws Exception {
        mockMvc.perform(get("/pet/getListPets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void givenNoPetsInDatabase_whenPetAddedItIsAddedIsCorrect() throws Exception {
        Pet testPet = new Pet();
        testPet.setId(1L);
        testPet.setPetType(PetType.CAT);
        testPet.setName("Мурзик");
        testPet.setStatusOfAdoption(StatusOfAdoption.WITHOUT_PARENT);

        mockMvc.perform(post("/pet/create")
                        .content(objectMapper.writeValueAsString(testPet))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.petType").value("CAT"))
                .andExpect(jsonPath("$.name").value("Мурзик"))
                .andExpect(jsonPath("$.statusOfAdoption").value("WITHOUT_PARENT"));
    }

    @Test
    void givenPetsInDatabase_thenPetFoundById() throws Exception {
        Pet testPet = new Pet();
        testPet.setId(1L);
        testPet.setPetType(PetType.CAT);
        testPet.setName("Мурзик");
        testPet.setStatusOfAdoption(StatusOfAdoption.WITHOUT_PARENT);

        mockMvc.perform(post("/pet/create")
                        .content(objectMapper.writeValueAsString(testPet))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.petType").value("CAT"))
                .andExpect(jsonPath("$.name").value("Мурзик"))
                .andExpect(jsonPath("$.statusOfAdoption").value("WITHOUT_PARENT"));

        mockMvc.perform(get("/pet/read/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Мурзик"));
    }

    @Test
    void givenPetCreated_whenPetIsEdited_thenPetChangedInDatabase() throws Exception {
        Pet testPet = new Pet(1L, null, null,PetType.CAT,"Мурзик",
                null, null,null,null,StatusOfAdoption.WITHOUT_PARENT);

        mockMvc.perform(post("/pet/create")
                        .content(objectMapper.writeValueAsString(testPet))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.petType").value("CAT"))
                .andExpect(jsonPath("$.name").value("Мурзик"))
                .andExpect(jsonPath("$.statusOfAdoption").value("WITHOUT_PARENT"));

        testPet.setStatusOfAdoption(StatusOfAdoption.IN_PROCESS);
        testPet.setDateOfAdoption(LocalDate.of(2022,06,15));

        Long id = testPet.getId();

        mockMvc.perform(put("/pet/update/{id}", id)
                        .content(objectMapper.writeValueAsString(testPet))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Мурзик"))
                .andExpect(jsonPath("$.statusOfAdoption").value("IN_PROCESS"))
                .andExpect(jsonPath("$.dateOfAdoption").value
                        (LocalDate.of(2022, 06, 15).format(DateTimeFormatter.ISO_LOCAL_DATE)));

        mockMvc.perform(get("/pet/read/{id}", id))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Мурзик"))
                .andExpect(jsonPath("$.statusOfAdoption").value("IN_PROCESS"))
                .andExpect(jsonPath("$.dateOfAdoption").value
                        (LocalDate.of(2022, 06, 15).format(DateTimeFormatter.ISO_LOCAL_DATE)));

    }

    @Test
    void givenPetsInDatabase_thenItIsDeletedByIdCorrectly() throws Exception {
        Pet testPet = new Pet();
        testPet.setId(1L);
        testPet.setPetType(PetType.CAT);
        testPet.setName("Мурзик");
        testPet.setStatusOfAdoption(StatusOfAdoption.WITHOUT_PARENT);
        mockMvc.perform(post("/pet/create")
                        .content(objectMapper.writeValueAsString(testPet))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Мурзик"));

        long id = testPet.getId();

        mockMvc.perform(delete("/pet/delete/{id}", id))
                .andExpect(status().isOk());

        mockMvc.perform(get("/pet"))
                .andExpect(status().isNotFound());
    }

}
