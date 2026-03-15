package com.htm.e20nomics.user.controller;

import com.htm.e20nomics.auth.domain.CustomUserDetails;
import com.htm.e20nomics.term.dto.MyTermResponse;
import com.htm.e20nomics.term.dto.MyTermCreateRequest;
import com.htm.e20nomics.user.dto.MyTermDetailResponse;
import com.htm.e20nomics.user.dto.MyTermUpdateRequest;
import com.htm.e20nomics.user.service.MyTermService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyTermController {

    public final MyTermService myTermsService;

    // 단어 추가하기
    @PostMapping("/api/me/terms")
    public ResponseEntity<Void> saveTerm(@Valid @RequestBody MyTermCreateRequest dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        myTermsService.saveTerm(dto.getTerm(), dto.getDefinition(), userDetails.getUserId());
        return ResponseEntity.status(201).build();
    }

    // 개인 단어 가져오기
    @GetMapping("/api/me/terms")
    public List<MyTermResponse> myTerms(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return myTermsService.myTerms(userDetails.getUserId());
    }

    // 개인 단어 상세페이지
    @GetMapping("/api/me/terms/{id}")
    public MyTermDetailResponse getTermDetail(@PathVariable Long id) {
        return myTermsService.getTermDetail(id);
    }

    // 공용 단어 수정하기


    // 개인 단어 수정
    @PutMapping("/api/me/terms/{id}")
    public ResponseEntity<Void> updateMyTerm(@PathVariable Long id,
                                             @Valid @RequestBody MyTermUpdateRequest dto,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        myTermsService.updateMyTerm(id, dto, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }

    // 개인 단어 삭제
    @DeleteMapping("/api/me/terms/{id}")
    public ResponseEntity<Void> deleteMyTerm(@PathVariable Long id,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        myTermsService.deleteMyTerm(id, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }
}
