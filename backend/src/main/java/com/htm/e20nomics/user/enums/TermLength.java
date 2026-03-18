package com.htm.e20nomics.user.enums;

public enum TermLength {
    SHORT("1문장"),
    MEDIUM("2문장"),
    LONG("3문장");

    private final String promptValue;

    TermLength(String promptValue) {
        this.promptValue = promptValue;
    }

    public String getPromptValue() {
        return promptValue;
    }
}
