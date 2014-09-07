package com.bitsworking.videoshowcase.datatypes;


import java.net.URI;

/**
 * Created by Chris Hager <chris@linuxuser.at> on 07/09/14.
 */
public class ShowcaseItem {
    public enum SHOWCASE_ITEM_TYPE {
        VIDEO_LOCAL,
        VIDEO_REMOTE,
        IMAGE,
        LINK
    }

    private SHOWCASE_ITEM_TYPE type = SHOWCASE_ITEM_TYPE.VIDEO_LOCAL;
    private String resource = null;
    private String thumbnail = null;
    private String text = "";

    public ShowcaseItem(SHOWCASE_ITEM_TYPE type, String resource, String thumbnail) {
        this.type = type;
        this.resource = resource;
        this.thumbnail = thumbnail;

        this.text = new String(resource);
        this.text = this.text.substring(this.text.indexOf("_") + 1);
        this.text = this.text.substring(0, this.text.lastIndexOf("."));
        this.text = this.text.replaceAll("[-_]", " ");
    }

    @Override
    public String toString() {
        return getClass().getName() + String.format("[%s, %s, %s, '%s']", this.type, this.resource, this.thumbnail, this.text);
    }
}
