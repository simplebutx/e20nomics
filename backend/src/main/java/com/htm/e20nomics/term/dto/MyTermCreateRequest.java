package com.htm.e20nomics.term.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MyTermCreateRequest {
    @NotBlank
    String term;

    @NotBlank
    String definition;

}
