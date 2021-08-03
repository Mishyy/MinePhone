package com.celerry.minephone.util.enums;

import com.celerry.minephone.MinePhone;

public enum App {
    SETTINGS_NAME,
    SETTINGS_BASE64,

    CLOCK_NAME,
    CLOCK_BASE64,

    MUSIC_NAME,
    MUSIC_BASE64,

    BACK_BASE64,
    NEXT_BASE64,

    MOON_BASE64,
    ;

    private final String path;

    App() {
        this.path = name().toLowerCase().replace("_", ".");
    }

    @Override
    public String toString() {
        return MinePhone.getPlugin().getConfig().getString("apps." + path);
    }
}
