package ru.skyteam.pettelegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skyteam.pettelegrambot.entity.Parent;
import ru.skyteam.pettelegrambot.entity.Pet;
import ru.skyteam.pettelegrambot.entity.Report;
import ru.skyteam.pettelegrambot.entity.Volunteer;
import ru.skyteam.pettelegrambot.repository.ReportRepository;
import ru.skyteam.pettelegrambot.service.ReportService;

import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {
    private final ReportService reportService;
    private final ReportRepository reportRepository;

    public ReportController(ReportService reportService, ReportRepository reportRepository) {
        this.reportService = reportService;
        this.reportRepository = reportRepository;
    }

    @Operation(summary = "Внести отчет в базу",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отчет создан",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report.class)
                            )
                    )
            })
    @PostMapping("/create")
    public Report create(@RequestBody Report report) {
        return reportService.create(report);
    }

    @Operation(summary = "Найти отчет по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отчет найден",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Report.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report.class)
                            )
                    )
            })
    @GetMapping("/read/{id}")
    public Report read(@PathVariable long id) {return reportService.read(id);}

    @Operation(
            summary = "Изменить запись об отчете",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Данные об отчете изменены",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report.class)
                            )
                    )
            })
    @PutMapping("/update/{id}")
    public ResponseEntity<Report> update(@RequestBody Report report) {
        Report report1 = reportService.save(report);
        if (report1 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(report1);
    }

    @Operation(
            summary = "Удалить отчет",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отчет удален",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report.class)
                            )
                    )
            })

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable long id) {reportService.delete(id);}

    @Operation(
            summary = "Получить список отчетов",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список отчетов получен",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Report.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Report.class))
                            )
                    )
            })
    @GetMapping("/getListReports")
    public List<Report> read() {
        return reportService.readAll();
    }

    @Operation(
            summary = "Сохранить отчет",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отчет сохранен",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Report.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Report.class))
                            )
                    )
            })
    @GetMapping("/save")
    public void save (@PathVariable Report report) {
        reportService.save(report);
    }

    @Operation(
            summary = "Найти последний отчет усыновителя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отчет найден",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Report.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Report.class))
                            )
                    )
            })
    @GetMapping("/reportFindLastByParent")
    public void reportFindLastByParent(@PathVariable Parent parent ) {
        reportService.reportFindLastByParent(parent);
    }
}

