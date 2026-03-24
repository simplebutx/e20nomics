package com.htm.e20nomics.global.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OpenAiImageResponse {
    private List<ImageData> data;

    @Getter
    @NoArgsConstructor
    public static class ImageData {
        @JsonProperty("b64_json")
        private String b64Json;
    }
}
