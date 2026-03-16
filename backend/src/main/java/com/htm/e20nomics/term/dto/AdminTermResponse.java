package com.htm.e20nomics.term.dto;

import com.htm.e20nomics.term.domain.AdminTerm;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminTermResponse {

    private final Long id;
    private final String term;
    private final String definition;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public AdminTermResponse(AdminTerm adminTerm) {
        this.id = adminTerm.getId();
        this.term = adminTerm.getTerm();
        this.definition = adminTerm.getDefinition();
        this.createdAt = adminTerm.getCreatedAt();
        this.updatedAt = adminTerm.getUpdatedAt();
    }
}
