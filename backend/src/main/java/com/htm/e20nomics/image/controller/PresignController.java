package com.htm.e20nomics.image.controller;

import com.htm.e20nomics.image.dto.PresignRequest;
import com.htm.e20nomics.image.service.PresignService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PresignController {
    private final PresignService presignService;

    @RequestMapping("/api/admin/images/presign")
    public PresignService.PresignResponse presign(@RequestBody PresignRequest dto) {
        return presignService.createPresignedUrl(dto.getContentType());
    }
}
