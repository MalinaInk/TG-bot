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
            summary = "create volunteer",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "volunteer created",
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

    @PostMapping("/create")
    public Volunteer create(@RequestBody Volunteer volunteer) {
        return volunteerService.create(volunteer);
    }
    @Operation(
            summary = "read volunteer by id",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "volunteer found",
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

    @GetMapping("/read/{id}")
    public Volunteer read(@PathVariable long id) {
        return volunteerService.read(id);
    }

    @Operation(
            summary = "update volunteer",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "volunteer updated",
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
    public ResponseEntity<Volunteer> update(@RequestBody Volunteer volunteer) {
        Volunteer volunteer1 = volunteerService.update(volunteer);
        if (volunteer1 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(volunteer1);
    }

    @Operation(
            summary = "delete volunteer",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "volunteer deleted",
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
    public void delete( @PathVariable long id) {
        volunteerService.delete(id);
    }

    @Operation(
            summary = "get list volunteers",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "all volunteers",
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
    @GetMapping("/getListVolunteers")
    public List getAllVolunteer() {
        return volunteerService.getAll();
    }
}