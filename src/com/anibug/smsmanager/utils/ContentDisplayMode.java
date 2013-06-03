package com.anibug.smsmanager.utils;

public enum ContentDisplayMode implements TextFilter {
    NORMAL (0),
    DEMO (1);

    private final int index;

    ContentDisplayMode(int index) {
        this.index = index;
    }

    @Override
    public String filterText(String text) {
        if (text == null || text.length() == 0)
            return text;

        switch (index) {
            case 0: return text;
            case 1: return "Demo text";
            default:return "";
        }
    }
}
