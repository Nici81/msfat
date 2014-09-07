package com.bitsworking.videoshowcase.datatypes;


import android.net.Uri;

/**
 * Created by Chris Hager <chris@linuxuser.at> on 07/09/14.
 */
public class ShowcaseItem {
    public enum SHOWCASE_ITEM_TYPE {
        VIDEO_LOCAL,
//        VIDEO_REMOTE,
//        IMAGE,
//        LINK
    }

    public SHOWCASE_ITEM_TYPE type = SHOWCASE_ITEM_TYPE.VIDEO_LOCAL;
    public String resourceFn = null;
    public String thumbnailFn = null;
    public String directory = null;
    public String text = "";

    public ShowcaseItem(SHOWCASE_ITEM_TYPE type, String directory, String resourceFn, String thumbnailFn) {
        this.type = type;
        this.directory = directory;
        this.resourceFn = resourceFn;
        this.thumbnailFn = thumbnailFn;

        this.text = new String(resourceFn);
        this.text = this.text.substring(this.text.indexOf("_") + 1);
        this.text = this.text.substring(0, this.text.lastIndexOf("."));
        this.text = this.text.replaceAll("[-_]", " ");
    }

    @Override
    public String toString() {
        return getClass().getName() + String.format("[%s, %s, %s, '%s']", this.type, this.resourceFn, this.thumbnailFn, this.text);
    }

    /* Returns an Uri like Uri.parse("file:///storage/emulated/0/videos/video1.mp4") */
    public Uri getResourceUri() {
        return Uri.parse("file://" + directory + "/" + resourceFn);
    }

    /* Returns a full filename like `/storage/emulated/0/videos/video1.mp4` */
    public String getThumbnailFullFilename() {
        return (thumbnailFn == null) ? null : directory + "/" + thumbnailFn;
    }
}