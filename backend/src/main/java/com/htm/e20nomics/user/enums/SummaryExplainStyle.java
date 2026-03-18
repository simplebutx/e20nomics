package com.htm.e20nomics.user.enums;

public enum SummaryExplainStyle {
    CORE_ONLY("핵심만 간단하게"),
    WITH_BACKGROUND("배경도 함께 설명"),
    CAUSE_AND_EFFECT("원인과 결과 위주로");

    private final String promptValue;

    SummaryExplainStyle(String promptValue) {
        this.promptValue = promptValue;
    }

    public String getPromptValue() {
        return promptValue;
    }
}
