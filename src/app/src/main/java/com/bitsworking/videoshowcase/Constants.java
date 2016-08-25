package com.bitsworking.videoshowcase;

public interface Constants {
    static final boolean INTERNAL_BUILD = !("@CONFIG.INTERNAL@".equals("false"));
    static final String BUILD_NUMBER = "@CONFIG.BUILD_NUMBER@";

    static final String SD_DIRECTORY_VIDEOS = "/VIDEOSHOWCASE";
    static final String PASSWORD_EXIT = "medex14";

    static final String LINK_SPENDEN = "https://www.aerzte-ohne-grenzen.at/videoshowcase-spenden";
    static final String LINK_NEWSLETTER = "https://www.aerzte-ohne-grenzen.at/newsletter/newsletter-abonnieren";
    static final String LINK_BREAKTHESILENCE = "http://www.aerzte-ohne-grenzen.at/videoshowcase-link";

    static final Boolean KIOSK_MODE_DISALLOW_APP_SWITCHING = true;
    static final Boolean KEEP_SCREEN_ON = true;
    static final Boolean HIDE_SYSTEM_UI = false;
    static final Boolean USE_WAKE_LOCK = false;

    static final String URL_UPDATE_VERSION = "http://www.bitsworking.at/msf/app.version";
    static final String URL_UPDATE_APK = "http://www.bitsworking.at/msf/app.apk";
}
