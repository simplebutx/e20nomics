package com.htm.e20nomics.term.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MyTermResponse {
    private Long id;
    private String term;
    private String definition;
}
