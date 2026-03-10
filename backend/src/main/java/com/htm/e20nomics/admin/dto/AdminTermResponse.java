package com.htm.e20nomics.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AdminTermResponse {
    private Long id;
    private String term;
    private String definition;
}
