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
import ru.skyteam.pettelegrambot.repository.ParentRepository;
import ru.skyteam.pettelegrambot.service.ParentService;

import java.util.List;

@RestController
@RequestMapping("/parent")
public class ParentController {
    private final ParentService parentService;
    private final ParentRepository parentRepository;

    public ParentController(ParentService parentService, ParentRepository parentRepository) {
        this.parentService = parentService;
        this.parentRepository = parentRepository;
    }

    @Operation(summary = "Создать усыновителя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Усыновитель занесен в базу",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Parent.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Parent.class)
                            )
                    )
            })
    @PostMapping("/create")
    public Parent create(@RequestBody Parent parent) {
        return parentService.create(parent);
    }

    @Operation(summary = "Найти усыновителя по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Усыновитель найден",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Parent.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Parent.class)
                            )
                    )
            })
    @GetMapping("/read/{id}")
    public Parent read (@PathVariable long id) {return parentService.read(id);}

    @Operation(
            summary = "Изменить данные об усыновителе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Данные об усыновителе успешно изменены",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Parent.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Parent.class)
                            )
                    )
            })
    @PutMapping("/update/{id}")
    public ResponseEntity<Parent> update(@RequestBody Parent parent) {
        Parent parent1 = parentService.update(parent);
        if (parent1 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(parent1);

    }

    @Operation(
            summary = "Удалить данные усыновителя из базы",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Данные успешно удалены",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Parent.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Parent.class)
                            )
                    )
            })

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable long id) {parentService.delete(id);}

    @Operation(
            summary = "Получить список усыновиетелей",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список усыновиетелей получен",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Parent.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Parent.class))
                            )
                    )
            })
    @GetMapping("/getListParents")
    public List<Parent> read() {
        return parentService.readAll();
    }

    @Operation(
            summary = "Найти усыновителя по ChatId",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Усыновитель найден",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Parent.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Parent.class))
                            )
                    )
            })

    @GetMapping("/findParentByChatId")
    public Parent findParentByChatId(Long chatId) {
        return parentRepository.getParentByChatId(chatId);
    }
}