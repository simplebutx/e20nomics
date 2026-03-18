package com.htm.e20nomics.user.enums;

public enum SummaryDifficulty {
    EASY("쉽게"),
    NORMAL("적당한 난이도"),
    HARD("난이도 조정없이 그대로");

    private final String promptValue;

    SummaryDifficulty(String promptValue) {
        this.promptValue = promptValue;
    }

    public String getPromptValue() {
        return promptValue;
    }
}
