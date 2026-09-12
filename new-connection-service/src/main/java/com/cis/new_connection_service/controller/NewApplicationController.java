package com.cis.new_connection_service.controller;

import com.cis.new_connection_service.dto.ConnectionRequestDto;
import com.cis.new_connection_service.dto.ConnectionResponseDto;
import com.cis.new_connection_service.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/connections")
@RestController
@CrossOrigin("*")
public class NewApplicationController {

    @Autowired
    private ConnectionService connectionService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<ConnectionRequestDto> createConnection(
            @RequestBody ConnectionRequestDto request,
            Authentication authentication
    ) {
        System.out.println("Authenticated username: " + authentication.getName());
        System.out.println("Authorities: " + authentication.getAuthorities());
        ConnectionRequestDto response =
                connectionService.createConnection(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<ConnectionResponseDto>> getConnectionDetails(){
        List<ConnectionResponseDto> connectionResposeDtoList=connectionService.getAllPendingConnectionDetails();
        return new ResponseEntity<>(connectionResposeDtoList,HttpStatus.OK);
    }
}
