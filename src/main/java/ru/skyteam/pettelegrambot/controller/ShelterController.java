package ru.skyteam.pettelegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skyteam.pettelegrambot.entity.Shelter;
import ru.skyteam.pettelegrambot.service.ShelterService;

import java.util.List;

@RestController
@RequestMapping("/shelter")

public class ShelterController {
    private final ShelterService shelterService;

    ShelterController(ShelterService shelterService) {
        this.shelterService = shelterService;
    }

    @Operation(
            summary = "Создать приют",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Приют создан",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Shelter.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Shelter.class)
                            )
                    )
            })

    @PostMapping("/create")
    public Shelter create(@RequestBody Shelter shelter) {
        return shelterService.create(shelter);
    }

    @Operation(
            summary = "Получить приют по id",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Приют найден",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Shelter.class)
                    )
            ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Shelter.class)
                            )
                    )
            })

    @GetMapping("/read/{id}")
    public Shelter read(@PathVariable long id) {
        return shelterService.read(id);
    }

    @Operation(
            summary = "Изменить данные о приюте",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Данные о приюте успешно изменены",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Shelter.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Shelter.class)
                            )
                    )
            })

    @PutMapping("/update/{id}")
    public ResponseEntity<Shelter> update(@RequestBody Shelter shelter) {
        Shelter shelter1 = shelterService.update(shelter);
        if (shelter1 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(shelter1);
    }

    @Operation(
            summary = "Удалить приют",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Приют удален",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Shelter.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Shelter.class)
                            )
                    )
            })

    @DeleteMapping("/delete/{id}")
    public void delete( @PathVariable long id) {
        shelterService.delete(id);
    }

    @Operation(
            summary = "Получить список приютов",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список приютов получен",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Shelter.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Shelter.class))
                            )
                    )
            })
    @GetMapping("/getListShelters")
    public List getAllShelter() {
        return shelterService.readAll();
    }
}
