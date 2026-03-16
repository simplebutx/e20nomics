package com.htm.e20nomics.term.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyTermGenerateResponse {
    private String term;
    private String definition;
    private Boolean canSave;
}
