package me.fi_calculator.fi_calculator.controller.api;

import me.fi_calculator.fi_calculator.domain.generic.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/fi", produces = MediaType.APPLICATION_JSON_VALUE)
public class CalculatorController {

    @PostMapping(path = "/calculate", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ApiResponse<?>> calculate(){
        return null;
    }
}
