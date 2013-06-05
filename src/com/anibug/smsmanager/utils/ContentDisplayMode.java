package com.anibug.smsmanager.utils;

public enum ContentDisplayMode implements TextMasker {
    NORMAL (0),
    DEMO (1);

    private final int index;

    ContentDisplayMode(int index) {
        this.index = index;
    }

    @Override
    public String maskText(String text) {
        if (text == null || text.length() == 0)
            return text;

        switch (index) {
            case 0: return text;
            case 1: return "Demo text";
            default:return "";
        }
    }

    @Override
    public String maskNumber(String number) {
        if (number == null || number.length() == 0)
            return number;

        final int UNMASK_LENGTH = 3;
        switch (index) {
            case 0: return number;
            case 1: return number.length() < UNMASK_LENGTH ? "..." : number.substring(0, UNMASK_LENGTH) + "...";
            default:return "";
        }
    }
}
