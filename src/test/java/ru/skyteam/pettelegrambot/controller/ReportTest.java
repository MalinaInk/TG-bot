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
import ru.skyteam.pettelegrambot.repository.ParentRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)


public class ReportTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ParentRepository parentRepository;

    @Test
    void givenNoReportsInDatabase_whenGetReports_thenEmptyJsonArray() throws Exception {
        mockMvc.perform(get("/report/getListReports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void givenNoReportsInDatabase_whenReportAddedItIsAddedIsCorrect() throws Exception {
        Parent testParent = new Parent(123456L, "Глебова Екатерина", "8-888-888-8888");
        testParent.setId(1L);
        parentRepository.save(testParent);

        Report testReport = new Report(1L,LocalDate.of(2022,06,15), null, testParent,
                "Диета", "Самочувствие", "Привычки","Фото", true, LastAction.DONE );

        mockMvc.perform(post("/report/create")
                        .content(objectMapper.writeValueAsString(testReport))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(1L))
               // .andExpect(jsonPath("$.pet.id").value(1L))
                .andExpect(jsonPath("$.parent.id").value(1L))
                .andExpect(jsonPath("$.petDiet").value("Диета"))
                .andExpect(jsonPath("$.health").value("Самочувствие"))
                .andExpect(jsonPath("$.changingHabits").value("Привычки"))
                .andExpect(jsonPath("$.pathToPhoto").value("Фото"))
                .andExpect(jsonPath("$.isCorrect").value(true))
                .andExpect(jsonPath("$.lastAction").value("DONE"))
                .andExpect(jsonPath("$.reportDate").isNotEmpty())
                .andExpect(jsonPath("$.reportDate").value
                       (LocalDate.of(2022, 06, 15).format(DateTimeFormatter.ISO_LOCAL_DATE)));
    }

    @Test
    void givenReportsInDatabase_thenReportFoundById() throws Exception {

        Parent testParent = new Parent(123456L, "Глебова Екатерина", "8-888-888-8888");
        testParent.setId(1L);

        parentRepository.save(testParent);

        Report testReport = new Report(1L,LocalDate.of(2022,06,15), null, testParent,
                "Диета", "Самочувствие", "Привычки","Фото", true, LastAction.DONE );

        mockMvc.perform(post("/report/create")
                        .content(objectMapper.writeValueAsString(testReport))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.parent.id").value(1L))
                .andExpect(jsonPath("$.petDiet").value("Диета"))
                .andExpect(jsonPath("$.health").value("Самочувствие"))
                .andExpect(jsonPath("$.changingHabits").value("Привычки"))
                .andExpect(jsonPath("$.pathToPhoto").value("Фото"))
                .andExpect(jsonPath("$.isCorrect").value(true))
                .andExpect(jsonPath("$.lastAction").value("DONE"))
                .andExpect(jsonPath("$.reportDate").isNotEmpty())
                .andExpect(jsonPath("$.reportDate").value
                        (LocalDate.of(2022, 06, 15).format(DateTimeFormatter.ISO_LOCAL_DATE)));

        mockMvc.perform(get("/report/read/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.parent.id").value(1L))
                .andExpect(jsonPath("$.petDiet").value("Диета"))
                .andExpect(jsonPath("$.health").value("Самочувствие"))
                .andExpect(jsonPath("$.changingHabits").value("Привычки"))
                .andExpect(jsonPath("$.pathToPhoto").value("Фото"))
                .andExpect(jsonPath("$.isCorrect").value(true))
                .andExpect(jsonPath("$.lastAction").value("DONE"))
                .andExpect(jsonPath("$.reportDate").isNotEmpty())
                .andExpect(jsonPath("$.reportDate").value
                        (LocalDate.of(2022, 06, 15).format(DateTimeFormatter.ISO_LOCAL_DATE)));
    }

    @Test
    void givenReportInDatabase_whenReportEdited_thenItIsChangedInDatabase() throws Exception {

        Parent testParent = new Parent(123456L, "Глебова Екатерина", "8-888-888-8888");
        testParent.setId(1L);

        parentRepository.save(testParent);

        Report testReport = new Report(1L,LocalDate.of(2022,06,15),null, testParent,
                "Диета", "Самочувствие", "Привычки","Фото", true, LastAction.DONE );

        mockMvc.perform(post("/report/create")
                        .content(objectMapper.writeValueAsString(testReport))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.parent.id").value(1L))
                .andExpect(jsonPath("$.petDiet").value("Диета"))
                .andExpect(jsonPath("$.health").value("Самочувствие"))
                .andExpect(jsonPath("$.changingHabits").value("Привычки"))
                .andExpect(jsonPath("$.pathToPhoto").value("Фото"))
                .andExpect(jsonPath("$.isCorrect").value(true))
                .andExpect(jsonPath("$.lastAction").value("DONE"))
                .andExpect(jsonPath("$.reportDate").isNotEmpty())
                .andExpect(jsonPath("$.reportDate").value
                        (LocalDate.of(2022, 06, 15).format(DateTimeFormatter.ISO_LOCAL_DATE)));

        Report updatedReport = new Report(1L,LocalDate.of(2022,06,15), null, testParent,
                "Диета", "Самочувствие", "Привычки","Не соответствует", false, LastAction.WAITING_PHOTO );
        updatedReport.setId(testReport.getId());

        long id = updatedReport.getId();

        mockMvc.perform(put("/report/update/{id}", id)
                        .content(objectMapper.writeValueAsString(updatedReport))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.pathToPhoto").value("Не соответствует"))
                .andExpect(jsonPath("$.isCorrect").value(false))
                .andExpect(jsonPath("$.lastAction").value("WAITING_PHOTO"));

        mockMvc.perform(get("/report/read/{id}", id))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.parent.id").value(1L))
                .andExpect(jsonPath("$.petDiet").value("Диета"))
                .andExpect(jsonPath("$.health").value("Самочувствие"))
                .andExpect(jsonPath("$.changingHabits").value("Привычки"))
                .andExpect(jsonPath("$.pathToPhoto").value("Не соответствует"))
                .andExpect(jsonPath("$.isCorrect").value(false))
                .andExpect(jsonPath("$.lastAction").value("WAITING_PHOTO"))
                .andExpect(jsonPath("$.reportDate").isNotEmpty())
                .andExpect(jsonPath("$.reportDate").value
                        (LocalDate.of(2022, 06, 15).format(DateTimeFormatter.ISO_LOCAL_DATE)));

    }

    @Test
    void givenReportInDatabase_thenReportDeletedByIdIsCorrect() throws Exception {

        Parent testParent = new Parent(123456L, "Глебова Екатерина", "8-888-888-8888");
        testParent.setId(1L);

        parentRepository.save(testParent);

        Report testReport = new Report(1L,LocalDate.of(2022,06,15), null, testParent,
                "Диета", "Самочувствие", "Привычки","Фото", true, LastAction.DONE );

        mockMvc.perform(post("/report/create")
                        .content(objectMapper.writeValueAsString(testReport))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.parent.id").value(1L))
                .andExpect(jsonPath("$.petDiet").value("Диета"))
                .andExpect(jsonPath("$.health").value("Самочувствие"))
                .andExpect(jsonPath("$.changingHabits").value("Привычки"))
                .andExpect(jsonPath("$.pathToPhoto").value("Фото"))
                .andExpect(jsonPath("$.isCorrect").value(true))
                .andExpect(jsonPath("$.lastAction").value("DONE"))
                .andExpect(jsonPath("$.reportDate").isNotEmpty())
                .andExpect(jsonPath("$.reportDate").value
                        (LocalDate.of(2022, 06, 15).format(DateTimeFormatter.ISO_LOCAL_DATE)));

        long id = testReport.getId();

        mockMvc.perform(delete("/report/delete/{id}", id))
                .andExpect(status().isOk());

        mockMvc.perform(get("/report"))
                .andExpect(status().isNotFound());
    }

}