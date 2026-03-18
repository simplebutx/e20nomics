package com.htm.e20nomics.user.enums;

public enum TermDifficulty {
    EASY("쉽게"),
    NORMAL("중간 난이도"),
    HARD("어렵게");

    private final String promptValue;

    TermDifficulty(String promptValue) {
        this.promptValue = promptValue;
    }

    public String getPromptValue() {
        return promptValue;
    }
}
