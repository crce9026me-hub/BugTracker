package com.company.bugtracker1.sla.controller;

import com.company.bugtracker1.sla.dto.SLAPolicyDto;
import com.company.bugtracker1.sla.service.SLAPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sla/policies")
@RequiredArgsConstructor
public class SLAPolicyController {

    private final SLAPolicyService slaPolicyService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AGENT')")
    public ResponseEntity<SLAPolicyDto.SLAPolicyResponse> getPolicyById(@PathVariable Long id) {
        SLAPolicyDto.SLAPolicyResponse response = slaPolicyService.getPolicyById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/project/{projectId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AGENT')")
    public ResponseEntity<List<SLAPolicyDto.SLAPolicyResponse>> getPoliciesByProjectId(
            @PathVariable Long projectId) {
        List<SLAPolicyDto.SLAPolicyResponse> responses = slaPolicyService.getPoliciesByProjectId(projectId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/project/{projectId}/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AGENT')")
    public ResponseEntity<List<SLAPolicyDto.SLAPolicyResponse>> getActivePoliciesByProjectId(
            @PathVariable Long projectId) {
        List<SLAPolicyDto.SLAPolicyResponse> responses = slaPolicyService.getActivePoliciesByProjectId(projectId);
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<SLAPolicyDto.SLAPolicyResponse> createPolicy(
            @RequestBody SLAPolicyDto.CreateSLAPolicyRequest request) {
        SLAPolicyDto.SLAPolicyResponse response = slaPolicyService.createPolicy(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<SLAPolicyDto.SLAPolicyResponse> updatePolicy(
            @PathVariable Long id,
            @RequestBody SLAPolicyDto.UpdateSLAPolicyRequest request) {
        SLAPolicyDto.SLAPolicyResponse response = slaPolicyService.updatePolicy(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> deletePolicy(@PathVariable Long id) {
        slaPolicyService.deletePolicy(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/project/{projectId}/initialize-defaults")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> initializeDefaultPolicies(@PathVariable Long projectId) {
        slaPolicyService.initializeDefaultPolicies(projectId);
        return ResponseEntity.ok().build();
    }
}
