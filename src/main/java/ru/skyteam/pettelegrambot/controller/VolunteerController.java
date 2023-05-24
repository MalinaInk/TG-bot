package ru.skyteam.pettelegrambot.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skyteam.pettelegrambot.entity.Volunteer;
import ru.skyteam.pettelegrambot.service.VolunteerService;

import java.util.List;

@RestController
@RequestMapping("/volunteer")

public class VolunteerController {
    private final VolunteerService volunteerService;

    VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @Operation(
            summary = "Создать заптсь о новом волонтере",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Волонтер создан",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Volunteer.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Volunteer.class)
                            )
                    )
            })

    @PostMapping("/create")
    public Volunteer create(@RequestBody Volunteer volunteer) {
        return volunteerService.create(volunteer);
    }

    @Operation(
            summary = "Найти волонтера по id",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Волонтер найден",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Volunteer.class)
                    )
            ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Volunteer.class)
                            )
                    )
            })

    @GetMapping("/read/{id}")
    public Volunteer read(@PathVariable long id) {
        return volunteerService.read(id);
    }

    @Operation(
            summary = "Изменить данные о волонтере",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Данные о волонтере успешно изменены",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Volunteer.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Volunteer.class)
                            )
                    )
            })

    @PutMapping("/update/{id}")
    public ResponseEntity<Volunteer> update(@RequestBody Volunteer volunteer) {
        Volunteer volunteer1 = volunteerService.update(volunteer);
        if (volunteer1 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(volunteer1);
    }

    @Operation(
            summary = "Удалить волонтера из базы",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Волонтер удален",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Volunteer.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Volunteer.class)
                            )
                    )
            })

    @DeleteMapping("/delete/{id}")
    public void delete( @PathVariable long id) {
        volunteerService.delete(id);
    }

    @Operation(
            summary = "Получить список волонтеров",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список волонтеров получен",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Volunteer.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Volunteer.class))
                            )
                    )
            })
    @GetMapping("/getListVolunteers")
    public List getAllVolunteer() {
        return volunteerService.getAll();
    }
}