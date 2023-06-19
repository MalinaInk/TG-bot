package ru.skyteam.pettelegrambot.controller;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.annotation.DirtiesContext;
import ru.skyteam.pettelegrambot.entity.*;
import ru.skyteam.pettelegrambot.repository.PetRepository;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)


public class ParentTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    PetRepository petRepository;
    @Autowired
    ObjectMapper objectMapper;


    @Test
    void givenNoParentsInDatabase_whenGetParents_thenEmptyJsonArray() throws Exception {
        mockMvc.perform(get("/parent/getListParents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void givenNoParentsInDatabase_whenParentAddedItIsAddedIsCorrect() throws Exception {

        Pet testPet = new Pet(1L,PetType.CAT,"Мурзик",null );
        List<Pet> pets = new ArrayList<Pet>();
        pets.add(testPet);
        petRepository.saveAll(pets);

        Parent testParent = new Parent(123456L, "Глебова Екатерина", "8-888-888-8888");
        testParent.setId(1L);
        testParent.setPets(pets);

        mockMvc.perform(post("/parent/create")
                        .content(objectMapper.writeValueAsString(testParent))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.chatId").isNotEmpty())
                .andExpect(jsonPath("$.chatId").isNumber())
                .andExpect(jsonPath("$.chatId").value(123456L))
                .andExpect(jsonPath("$.fullName").value("Глебова Екатерина"))
                .andExpect(jsonPath("$.phoneNumber").value("8-888-888-8888"))
                .andExpect(jsonPath("$.pets").isNotEmpty())
                .andExpect(jsonPath("$.pets").isArray())
                .andExpect(jsonPath("$.pets[*].name").value(hasItem("Мурзик")));
    }

    @Test
    void givenParentsInDatabase_thenParentFoundById() throws Exception {

        Parent testParent = new Parent(123456L, "Глебова Екатерина", "8-888-888-8888");
        testParent.setId(1L);

        mockMvc.perform(post("/parent/create")
                        .content(objectMapper.writeValueAsString(testParent))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.chatId").isNotEmpty())
                .andExpect(jsonPath("$.chatId").isNumber())
                .andExpect(jsonPath("$.chatId").value(123456L))
                .andExpect(jsonPath("$.fullName").value("Глебова Екатерина"))
                .andExpect(jsonPath("$.phoneNumber").value("8-888-888-8888"));

        mockMvc.perform(get("/parent/read/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.chatId").isNotEmpty())
                .andExpect(jsonPath("$.chatId").isNumber())
                .andExpect(jsonPath("$.chatId").value(123456L))
                .andExpect(jsonPath("$.fullName").value("Глебова Екатерина"))
                .andExpect(jsonPath("$.phoneNumber").value("8-888-888-8888"));
    }

    @Test
    void givenThereIsParentCreated_whenParentIsEdited_thenParentChangedInDatabase() throws Exception {

        Parent testParent = new Parent(1L, 123456L, "Глебова Екатерина", "8-888-888-8888",null);

        mockMvc.perform(post("/parent/create")
                        .content(objectMapper.writeValueAsString(testParent))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.chatId").isNotEmpty())
                .andExpect(jsonPath("$.chatId").isNumber())
                .andExpect(jsonPath("$.chatId").value(123456L))
                .andExpect(jsonPath("$.fullName").value("Глебова Екатерина"))
                .andExpect(jsonPath("$.phoneNumber").value("8-888-888-8888"));

        Parent updatedParent = new Parent(testParent.getId(), testParent.getChatId(), testParent.getFullName(), "8-999-999-9999",
                testParent.getPets());

        long id = updatedParent.getId();

        mockMvc.perform(put("/parent/update/{id}", id)
                        .content(objectMapper.writeValueAsString(updatedParent))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.chatId").isNotEmpty())
                .andExpect(jsonPath("$.chatId").isNumber())
                .andExpect(jsonPath("$.chatId").value(123456L))
                .andExpect(jsonPath("$.fullName").value("Глебова Екатерина"))
                .andExpect(jsonPath("$.phoneNumber").value("8-999-999-9999"));

        mockMvc.perform(get("/parent/read/{id}", id))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.chatId").isNotEmpty())
                .andExpect(jsonPath("$.chatId").isNumber())
                .andExpect(jsonPath("$.chatId").value(123456L))
                .andExpect(jsonPath("$.fullName").value("Глебова Екатерина"))
                .andExpect(jsonPath("$.phoneNumber").value("8-999-999-9999"));
    }

    @Test
    void givenPetsInDatabase_thenItIsDeletedByIdCorrectly() throws Exception {

        Parent testParent = new Parent(123456L, "Глебова Екатерина", "8-888-888-8888");
        testParent.setId(1L);

        mockMvc.perform(post("/parent/create")
                        .content(objectMapper.writeValueAsString(testParent))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.chatId").isNotEmpty())
                .andExpect(jsonPath("$.chatId").isNumber())
                .andExpect(jsonPath("$.chatId").value(123456L))
                .andExpect(jsonPath("$.fullName").value("Глебова Екатерина"))
                .andExpect(jsonPath("$.phoneNumber").value("8-888-888-8888"));

        long id = testParent.getId();

        mockMvc.perform(delete("/parent/delete/{id}", id))
                .andExpect(status().isOk());

        mockMvc.perform(get("/parent"))
                .andExpect(status().isNotFound());
    }

}