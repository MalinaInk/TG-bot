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
import ru.skyteam.pettelegrambot.repository.ShelterRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)


public class VolunteerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void givenNoVolunteersInDatabase_whenGetVolunteers_thenEmptyJsonArray() throws Exception {
        mockMvc.perform(get("/volunteer/getListVolunteers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void givenNoVolunteersInDatabase_whenVolunteerAddedItIsAddedIsCorrect() throws Exception {

        Volunteer testVolunteer = new Volunteer(1L, 0L,"Иванова Мария" );

        mockMvc.perform(post("/volunteer/create")
                        .content(objectMapper.writeValueAsString(testVolunteer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.chatId").isNotEmpty())
                .andExpect(jsonPath("$.chatId").isNumber())
                .andExpect(jsonPath("$.chatId").value(0L))
                .andExpect(jsonPath("$.fullName").value("Иванова Мария"));
    }

    @Test
    void givenVolunteersInDatabase_thenVolunteerFoundById() throws Exception {

        Volunteer testVolunteer = new Volunteer(1L, 0L,"Иванова Мария" );

        mockMvc.perform(post("/volunteer/create")
                        .content(objectMapper.writeValueAsString(testVolunteer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.chatId").isNotEmpty())
                .andExpect(jsonPath("$.chatId").isNumber())
                .andExpect(jsonPath("$.chatId").value(0L))
                .andExpect(jsonPath("$.fullName").value("Иванова Мария"));

        mockMvc.perform(get("/volunteer/read/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.chatId").isNotEmpty())
                .andExpect(jsonPath("$.chatId").isNumber())
                .andExpect(jsonPath("$.chatId").value(0L))
                .andExpect(jsonPath("$.fullName").value("Иванова Мария"));
    }

    @Test
    void givenVolunteerInDatabase_whenVolunteerIsEdited_thenVolunteerChangedInDatabase() throws Exception {

        Volunteer testVolunteer = new Volunteer(1L, 0L,"Иванова Мария");

        mockMvc.perform(post("/volunteer/create")
                        .content(objectMapper.writeValueAsString(testVolunteer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.chatId").isNotEmpty())
                .andExpect(jsonPath("$.chatId").isNumber())
                .andExpect(jsonPath("$.chatId").value(0L))
                .andExpect(jsonPath("$.fullName").value("Иванова Мария"));

        Volunteer updatedVolunteer = new Volunteer(1L, 124L,"Иванова Мария");
        updatedVolunteer.setId(testVolunteer.getId());

        long id = updatedVolunteer.getId();

        mockMvc.perform(put("/volunteer/update/{id}", id)
                        .content(objectMapper.writeValueAsString(updatedVolunteer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.chatId").isNotEmpty())
                .andExpect(jsonPath("$.chatId").isNumber())
                .andExpect(jsonPath("$.chatId").value(124L))
                .andExpect(jsonPath("$.fullName").value("Иванова Мария"));

        mockMvc.perform(get("/volunteer/read/{id}", id))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.chatId").isNotEmpty())
                .andExpect(jsonPath("$.chatId").isNumber())
                .andExpect(jsonPath("$.chatId").value(124L))
                .andExpect(jsonPath("$.fullName").value("Иванова Мария"));
    }

    @Test
    void givenVolunteersInDatabase_thenVolunteerDeletedByIdIsCorrect() throws Exception {
        Volunteer testVolunteer = new Volunteer(1L, 0L,"Иванова Мария");

        mockMvc.perform(post("/volunteer/create")
                        .content(objectMapper.writeValueAsString(testVolunteer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.chatId").isNotEmpty())
                .andExpect(jsonPath("$.chatId").isNumber())
                .andExpect(jsonPath("$.chatId").value(0L))
                .andExpect(jsonPath("$.fullName").value("Иванова Мария"));

        long id = testVolunteer.getId();

        mockMvc.perform(delete("/volunteer/delete/{id}", id))
                .andExpect(status().isOk());

        mockMvc.perform(get("/volunteer"))
                .andExpect(status().isNotFound());
    }

}