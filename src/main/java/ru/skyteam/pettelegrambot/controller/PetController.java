package ru.skyteam.pettelegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.skyteam.pettelegrambot.entity.Pet;
import ru.skyteam.pettelegrambot.repository.PetRepository;
import ru.skyteam.pettelegrambot.service.ParentService;
import ru.skyteam.pettelegrambot.service.PetService;
import java.util.List;

@RestController
@RequestMapping("/pet")
public class PetController {
    private final PetService petService;
    private final ParentService parentService;
    private final PetRepository petRepository;

    public PetController(PetService petService, ParentService parentService, PetRepository petRepository) {
        this.petService = petService;
        this.petRepository = petRepository;
        this.parentService = parentService;
    }

    @Operation(summary = "Внести данные о новом животном",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Новое животное создано",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pet.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pet.class)
                            )
                    )
            })
    @PostMapping("/create")
    public Pet create(@RequestBody Pet pet) {
        return petService.create(pet);
    }

    @Operation(summary = "Найти животное по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Животное найдено",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Pet.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pet.class)
                            )
                    )
            })
    @GetMapping("/read/{id}")
    public Pet read (@PathVariable long id) {return petService.read(id);}

    @Operation(
            summary = "Изменить данные о животном",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Данные о животном успешно изменены",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pet.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pet.class)
                            )
                    )
            })
    @PutMapping("/update")
    public Pet update(@PathVariable Pet pet) {
        return petRepository.save(pet);
    }

    @Operation(
            summary = "Удалить запись о животном",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Животное удалено из базы",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pet.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pet.class)
                            )
                    )
            })

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable long id) {
        petService.delete(id);
    }

    @Operation(
            summary = "Получить список животных",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список животных получен",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Pet.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Pet.class))
                            )
                    )
            })
    @GetMapping("/getListPets")
    public List<Pet> read() {
        return petService.readAll();
    }

    @Operation(
            summary = "Найти животное по id усыновителя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Животное найдено",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Pet.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Pet.class))
                            )
                    )
            })
    @GetMapping("/findByParentId")

    public List<Pet> findByParentId(Long parentId) {
        return petRepository.findAllByParentId(parentId);
    }

    @Operation(
            summary = "Список всех животных, соответсвующих chatId",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Все животные найдены",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Pet.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Pet.class))
                            )
                    )
            })
    @GetMapping("/findAllByChatId")
    public List<Pet> findAllByChatId(Long chatId) {
        return findByParentId(parentService.findParentByChatId(chatId).getId());
    }
}