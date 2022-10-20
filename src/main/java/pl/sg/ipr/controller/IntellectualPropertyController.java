package pl.sg.ipr.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import pl.sg.ipr.api.IntellectualProperty;

import java.util.List;

public interface IntellectualPropertyController {

    @Operation(summary = "Returns a list of all intellectual property entries for given domain",
            description = "",
            tags = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A list of all intellectual property entries for given domain",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = IntellectualProperty.class)))),

            @ApiResponse(responseCode = "400",
                    description = "Bad request, no domain in headers.",
                    content = @Content(mediaType = "plain/text",
                            schema = @Schema(implementation = String.class))),

            @ApiResponse(responseCode = "401",
                    description = "Not authenticated. Error message.",
                    content = @Content(mediaType = "plain/text",
                            schema = @Schema(implementation = String.class))),

            @ApiResponse(responseCode = "403",
                    description = "Not authorized. Error message.",
                    content = @Content(mediaType = "plain/text",
                            schema = @Schema(implementation = String.class)))})
    @GetMapping(produces = {"application/json", "plain/text"})
    List<IntellectualProperty> getAll(
            @Parameter(in = ParameterIn.HEADER,
                    description = "id of domain",
                    required = true,
                    content = @Content(mediaType = "plain/text",
                            schema = @Schema(implementation = Integer.class)))
            @RequestHeader(value = "domainId")
            int domainId
    );
}