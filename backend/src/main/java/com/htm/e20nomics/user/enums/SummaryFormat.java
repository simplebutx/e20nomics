package com.htm.e20nomics.user.enums;

public enum SummaryFormat {
    PARAGRAPH("문단형으로"),
    LIST("리스트형으로");

    private final String promptValue;

    SummaryFormat(String promptValue) {
        this.promptValue = promptValue;
    }

    public String getPromptValue() {
        return promptValue;
    }
}
