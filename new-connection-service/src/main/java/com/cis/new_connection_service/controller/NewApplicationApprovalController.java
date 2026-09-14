package com.cis.new_connection_service.controller;

import com.cis.new_connection_service.dto.ConnectionRequestDto;
import com.cis.new_connection_service.entity.ConsumerMasterVO;
import com.cis.new_connection_service.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/connections/approval")
public class NewApplicationApprovalController {

    @Autowired
    private ConnectionService connectionService;

    @PostMapping("/{customerId}/approved")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> approvedByCustomerId(@PathVariable("customerId") Long customerId){
        ConsumerMasterVO response=connectionService.approvedByCustomerId(customerId);
        return new ResponseEntity<>("Approved with account no: "+response.getAccountNo(), HttpStatus.OK);
    }

    @PostMapping("/{customerId}/reject")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> rejectByCustomerId(@PathVariable("customerId") Long customerId){
        String response=connectionService.rejectedByCustomerId(customerId);
        return new ResponseEntity<>("approved", HttpStatus.OK);
    }

    @PostMapping("/search")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ConnectionRequestDto> getApplicationStatus(@PathVariable @RequestParam("applicationNumber") String applicationNumber){
        ConnectionRequestDto connectionRequestDto=connectionService.searchWithApplicationNumber(applicationNumber);
        return new ResponseEntity<>(connectionRequestDto, HttpStatus.OK);
    }
}
