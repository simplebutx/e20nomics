package com.htm.e20nomics.term.controller;

import com.htm.e20nomics.term.dto.AdminTermCreateRequest;
import com.htm.e20nomics.term.dto.AdminTermGenerateRequest;
import com.htm.e20nomics.term.dto.AdminTermGenerateResponse;
import com.htm.e20nomics.term.dto.AdminTermResponse;
import com.htm.e20nomics.term.service.AdminTermService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/terms")
public class AdminTermController {

    private final AdminTermService adminTermService;

    @PostMapping("/generate")
    public AdminTermGenerateResponse generateTerm(@RequestBody AdminTermGenerateRequest dto) {
        return adminTermService.generateTerm(dto.getTerm());
    }

    @PostMapping
    public ResponseEntity<Void> createAdminTerm(@Valid @RequestBody AdminTermCreateRequest request) {
        adminTermService.createAdminTerm(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<AdminTermResponse>> getAdminTerms() {
        return ResponseEntity.ok(adminTermService.getAdminTerms());
    }

    @GetMapping("/{termId}")
    public ResponseEntity<AdminTermResponse> getAdminTerm(@PathVariable Long termId) {
        return ResponseEntity.ok(adminTermService.getAdminTerm(termId));
    }

    @PutMapping("/{termId}")
    public ResponseEntity<Void> updateAdminTerm(@PathVariable Long termId,
                                                @Valid @RequestBody AdminTermCreateRequest request) {
        adminTermService.updateAdminTerm(termId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{termId}")
    public ResponseEntity<Void> deleteAdminTerm(@PathVariable Long termId) {
        adminTermService.deleteAdminTerm(termId);
        return ResponseEntity.noContent().build();
    }
}
