package com.bitsworking.videoshowcase;

public interface Constants {
    static final boolean INTERNAL_BUILD = !("@CONFIG.INTERNAL@".equals("false"));
    static final String BUILD_NUMBER = "@CONFIG.BUILD_NUMBER@";

    static final String SD_DIRECTORY_VIDEOS = "/MSF-VIDEOSHOWCASE";
}