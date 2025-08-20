package me.fi_calculator.fi_calculator.controller.api;

import jakarta.validation.Valid;
import me.fi_calculator.fi_calculator.domain.dtos.FiRequest;
import me.fi_calculator.fi_calculator.domain.enums.EngineId;
import me.fi_calculator.fi_calculator.domain.generic.ApiResponse;
import me.fi_calculator.fi_calculator.services.calculator.models.FiCalcCommand;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/fi", produces = MediaType.APPLICATION_JSON_VALUE)
public class CalculatorController {

    @PostMapping(path = "/calculate", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ApiResponse<?>> calculate(    @Valid @RequestBody FiRequest req,
                                                        @RequestParam(defaultValue = "MONTE_CARLO") EngineId engine){
        var cmd = new FiCalcCommand(engine, req);

        return null;
    }
}
