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
import ru.skyteam.pettelegrambot.entity.Volunteer;
import ru.skyteam.pettelegrambot.service.ParentService;

import java.util.List;

@RestController
@RequestMapping("/parent")
public class ParentController {
    private final ParentService parentService;

    public ParentController(ParentService parentService) {
        this.parentService = parentService;

    }

    @Operation(summary = "create parent",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "parent created",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Parent.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "incorrect param",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Volunteer.class)
                            )
                    )
            })
    @PostMapping("/create")
    public Parent create(@RequestBody Parent parent) {
        return parentService.create(parent);
    }

    @Operation(summary = "read parent by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "parent found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Parent.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "incorrect param",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Volunteer.class)
                            )
                    )
            })
    @GetMapping("/read/{id}")
    public Parent read (@PathVariable long id) {
        return parentService.read(id);

    }

    @Operation(
            summary = "update parent",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "parent updated",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Volunteer.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "incorrect param",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Volunteer.class)
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
            summary = "delete parent",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "parent deleted",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Volunteer.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "incorrect param",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Volunteer.class)
                            )
                    )
            })

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable long id) {
        parentService.delete(id);

    }

    @Operation(
            summary = "get list parents",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "all parentss",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Volunteer.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "incorrect param",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Volunteer.class))
                            )
                    )
            })
    @GetMapping("/getListParents")
    public List<Parent> read() {
        return parentService.readAll();
    }

}