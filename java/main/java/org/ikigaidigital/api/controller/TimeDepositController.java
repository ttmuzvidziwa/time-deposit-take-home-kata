package org.ikigaidigital.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.ikigaidigital.domain.exception.DataConversionException;
import org.ikigaidigital.domain.exception.TimeDepositException;
import org.ikigaidigital.domain.model.dto.TimeDepositsDto;
import org.springframework.http.ResponseEntity;

@Tag(name = "XA Bank Time Deposit", description = "Endpoints for managing and retrieving XA Bank time deposit accounts.")
public interface TimeDepositController {
    @Operation(
            summary = "Update All Time Deposit Accounts",
            description = "Updates all time deposit accounts in the system. Returns a success message if the update is successful."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful update of all time deposit accounts",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Success!")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "An error occurred while updating time deposit accounts",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Update failed")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error message",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Unexpected error")
                    )
            )
    })
    ResponseEntity<?> updateAllTimeDepositAccounts();

    @Operation(summary = "Get All Time Deposit Accounts",
            description = "Retrieves all time deposit accounts with their balances and plan types.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful retrieval of time deposit accounts",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TimeDepositsDto.class),
                            examples = {@ExampleObject(value = """
                                    {
                                        "count": 3,
                                        "accounts": [
                                        {
                                                    "id": 1,
                                                    "planType": "BASIC",
                                                    "balance": 1000.00,
                                                    "days": 10
                                                },
                                                {
                                                    "id": 2,
                                                    "planType": "STUDENT",
                                                    "balance": 2000.00,
                                                    "days": 40
                                                },
                                                {
                                                    "id": 3,
                                                    "planType": "PREMIUM",
                                                    "balance": 5000.00,
                                                    "days": 50
                                                }
                                                ]
                                    }
                                    """),
                                    @ExampleObject(value = """
                                            {
                                                "count": 0,
                                                "accounts": []
                                            }
                                            """)
                            })),
            @ApiResponse(responseCode = "400", description = "Time deposit error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TimeDepositException.class))),
            @ApiResponse(responseCode = "400", description = "Data conversion error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataConversionException.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected server error message",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Exception.class),
                            examples = @ExampleObject(value = """
                                    "Unspecified error occurred"
                                    """)))
    })
    ResponseEntity<?> getAllTimeDepositAccounts();
}
