package com.bitsworking.videoshowcase;

public interface Constants {
    static final boolean INTERNAL_BUILD = !("@CONFIG.INTERNAL@".equals("false"));
    static final String BUILD_NUMBER = "@CONFIG.BUILD_NUMBER@";

    static final String SD_DIRECTORY_VIDEOS = "/VIDEOSHOWCASE";
    static final String PASSWORD_EXIT = "ente";

    static final Boolean HIDE_SYSTEM_UI = false;
    static final Boolean KEEP_SCREEN_ON = true;
    static final Boolean USE_WAKE_LOCK = false;
}