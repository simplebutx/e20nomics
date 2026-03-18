package com.htm.e20nomics.user.enums;

public enum SummaryLength {
    SHORT("3문장"),
    MEDIUM("4문장"),
    LONG("5문장");

    private final String promptValue;

    SummaryLength(String promptValue) {
        this.promptValue = promptValue;
    }

    public String getPromptValue() {
        return promptValue;
    }
}
