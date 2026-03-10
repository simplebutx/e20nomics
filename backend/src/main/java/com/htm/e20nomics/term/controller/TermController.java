package com.htm.e20nomics.term.controller;

import com.htm.e20nomics.auth.domain.CustomUserDetails;
import com.htm.e20nomics.term.dto.TermCreateRequest;
import com.htm.e20nomics.term.service.TermService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TermController {

    private final TermService termService;

    @PostMapping("/api/me/terms")
    public ResponseEntity<Void> saveTerm(@RequestBody TermCreateRequest dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        termService.saveTerm(dto.getTerm(), dto.getDefinition(), userDetails.getUserId());
        return ResponseEntity.status(201).build();
    }


    // 단어 수정하기

    // 단어 삭제하기
}
