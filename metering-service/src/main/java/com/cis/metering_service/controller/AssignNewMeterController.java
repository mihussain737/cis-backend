package com.cis.metering_service.controller;

import com.cis.metering_service.dto.ConsMtrRelRequest;
import com.cis.metering_service.entity.ConsMtrRel;
import com.cis.metering_service.service.impl.ConsMtrRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/metering/assign-new-meter")
public class AssignNewMeterController {

    @Autowired
    private ConsMtrRelService consMtrRelService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ConsMtrRel> assignNewMeter(@RequestBody ConsMtrRelRequest consMtrRelRequest) {
        ConsMtrRel assigned = consMtrRelService.assignMeter(consMtrRelRequest);
        return new ResponseEntity<>(assigned, HttpStatus.OK);
    }
}
